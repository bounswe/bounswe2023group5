// EnterVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { useMutation } from "react-query";
import { postCode, postEmail } from "../../Services/ForgotPassword";
import { useAuth } from "../../Components/Hooks/useAuth";

interface EnterVerificationCodeFormProps {
  isVerified: boolean;
  setIsVerified: (value: boolean) => void;
  email: string;
}

const EnterVerificationCodeForm: React.FC<EnterVerificationCodeFormProps> = ({
  setIsVerified,
  email,
}) => {
  const { setToken } = useAuth();
  const codeMutation = useMutation(postCode, {
    onSuccess: async (data) => {
      if (data.status === 400) {
        alert("Please enter a valid code");
        return;
      } else if (data.status === 500) {
        alert("Something went wrong.");
        return;
      }
      console.log(data);
      console.log(data.data);
      const token: string = data.data;
      setToken(token);

      setIsVerified(true);
    },
    onError: () => {
      alert("Something went wrong.");
    },
  });

  const onFinish = (data: any) => {
    // Logic for verifying the code
    data["userEmail"] = email;
    codeMutation.mutate(data);
  };


  const emailMutation = useMutation(postEmail, {
    onSuccess: (data) => {
      if (data.status === 404) {
        alert("Please enter a valid email address.");
        return;
      } else if (data.status === 500) {
        alert("Something went wrong.");
        return;
      } else if (data.status === 400) {
        alert("Bad Request");
        return;
      }
      alert("Verification code is sent to your email address.");
    },
    onError: () => {
      alert("Please enter a valid email address.");
    },
  });

  const onResend = () => {
    emailMutation.mutate(email);
  };

  return (
    <Form onFinish={onFinish} size="large">
      <Form.Item
        name="resetCode"
        rules={[
          { required: true, message: "Please enter the verification code" },
        ]}
      >
        <Input placeholder="Verification Code" />
      </Form.Item>
      <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
        <Button type="primary" onClick={onResend}>Resend Code</Button>
        <Button type="primary" htmlType="submit">
          Verify
        </Button>
      </div>
    </Form>
  );
};

export default EnterVerificationCodeForm;
