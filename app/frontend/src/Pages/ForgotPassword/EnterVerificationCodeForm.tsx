// EnterVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { useMutation } from "react-query";
import axios from 'axios';
import { postCode } from "../../Services/ForgotPassword";
import { useAuth } from "../../Components/Hooks/useAuth";

interface EnterVerificationCodeFormProps {
  isVerified: boolean;
  setIsVerified: (value: boolean) => void;
  email: string;
}

const EnterVerificationCodeForm: React.FC<EnterVerificationCodeFormProps> = ({
  setIsVerified,
  email
}) => {


    const {setToken} = useAuth();
    const codeMutation = useMutation(postCode,{  
    onSuccess: async (data) => {
        if(data.status === 400){
          alert("Please enter a valid code")
          return;
        }else if(data.status === 500){
          alert("Something went wrong.")
          return;
        }
        console.log(data);
        console.log(data.data);
        const token:string = data.data;
        setToken(token);
      
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
