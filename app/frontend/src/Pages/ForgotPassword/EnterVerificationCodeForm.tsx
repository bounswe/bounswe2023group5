// EnterVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { useMutation } from "react-query";
import axios from 'axios';

interface EnterVerificationCodeFormProps {
  isVerified: boolean;
  setIsVerified: (value: boolean) => void;
  email: string;
}

const EnterVerificationCodeForm: React.FC<EnterVerificationCodeFormProps> = ({
  setIsVerified,
  email
}) => {
    const postCode = async (data:any) => {
        return fetch(import.meta.env.VITE_APP_API_URL + "/api/auth/verify-reset-code", {
        method: "POST",
        headers: {
        "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });
    }

    const codeMutation = useMutation(postCode,{  
    onSuccess: async (data) => {
        if(data.status === 400){
          alert("Please enter a valid code")
          return;
        }else if(data.status === 500){
          alert("Something went wrong.")
          return;
        }
        
        const token:string = await data.text();

        axios.defaults.headers.common['Authorization'] = `${token}`;
        setIsVerified(true)    },
    onError: () => {
        alert("Something went wrong.")
    },});

  const onFinish = (data:any) => {
    // Logic for verifying the code
    data["userEmail"]=email;
    codeMutation.mutate( data );
  };

  return (
    <Form
      onFinish={onFinish}
      size="large"
    >
      <Form.Item
        name="resetCode"
        rules={[{ required: true, message: "Please enter the verification code" }]
      }>
        <Input placeholder="Verification Code" />
      </Form.Item>
      <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
        <Button type="primary">Resend Code</Button>
        <Button type="primary" htmlType="submit">
          Verify
        </Button>
      </div>
    </Form>
  );
};

export default EnterVerificationCodeForm;
