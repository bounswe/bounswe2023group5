// SendVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { MailOutlined } from "@ant-design/icons";

interface SendVerificationCodeFormProps {
  codeInputVisible: boolean;
  setCodeInputVisible: (value: boolean) => void;
}

const SendVerificationCodeForm: React.FC<SendVerificationCodeFormProps> = ({
  codeInputVisible,
  setCodeInputVisible,
}) => {
  const onFinish = () => {
    // Logic for sending verification code
    setCodeInputVisible(true)
    console.log("Verification code is sent");
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
