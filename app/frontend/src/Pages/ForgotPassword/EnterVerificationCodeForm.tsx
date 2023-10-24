// EnterVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";

interface EnterVerificationCodeFormProps {
  isVerified: boolean;
  setIsVerified: (value: boolean) => void;
}

const EnterVerificationCodeForm: React.FC<EnterVerificationCodeFormProps> = ({
  isVerified,
  setIsVerified,
}) => {
  const onFinish = () => {
    // Logic for verifying the code
    setIsVerified(true)
  };

  return (
    <Form
      onFinish={onFinish}
      size="large"
    >
      <Form.Item
        name="verification-code"
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
