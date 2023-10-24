// SendVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { MailOutlined } from "@ant-design/icons";
import { useMutation } from "react-query";


interface SendVerificationCodeFormProps {
  codeInputVisible: boolean;
  setCodeInputVisible: (value: boolean) => void;
  setEmail: (value: string) => void;
}

const SendVerificationCodeForm: React.FC<SendVerificationCodeFormProps> = ({
  codeInputVisible,
  setCodeInputVisible,
  setEmail,
}) => {

  const postEmail = async (email:string) => {
    return fetch(import.meta.env.VITE_APP_API_URL + "/api/auth/forgot-password?email="+ email, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
  }
  const emailMutation = useMutation(postEmail,{  
    onSuccess: (data) => {
      if(data.status === 404){
        alert("Please enter a valid email address.")
        return;
      }else if(data.status === 500){
        alert("Something went wrong.")
        return;
      }
      alert("Verification code is sent to your email address.")
      setCodeInputVisible(true)
    },
    onError: () => {
      alert("Please enter a valid email address.")
    },});




  const onFinish = (data:any) => {
    data["email"] = data["email"].toLowerCase();
    emailMutation.mutate( data.email );
    setEmail(data.email);
  };

  return (
    <Form
      layout="vertical"
      onFinish={onFinish}
      size="large"
    >
      <Form.Item
        style={{ width: "100%" }}
        name="email"
        rules={[
          { required: true, message: "Please enter your email" },
          { type: "email", message: "Please provide a valid email" },
        ]}
      >
        <Input
          prefix={<MailOutlined className="site-form-item-icon" />}
          placeholder="Email"
        />
      </Form.Item>
      <Form.Item>
        <Button
          disabled={codeInputVisible}
          type="primary"
          htmlType="submit"
        >
          Send Code
        </Button>
      </Form.Item>
    </Form>
  );
};

export default SendVerificationCodeForm;
