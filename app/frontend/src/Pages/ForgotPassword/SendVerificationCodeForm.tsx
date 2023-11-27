// SendVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { MailOutlined } from "@ant-design/icons";
import { useMutation } from "react-query";
import { postEmail } from "../../Services/ForgotPassword";
import { handleAxiosError } from "../../Library/utils/handleError";
import { NotificationUtil } from "../../Library/utils/notification";

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
  const emailMutation = useMutation(postEmail, {
    onSuccess: (data) => {
      if (data.status === 404) {
        NotificationUtil.error("Please enter a valid email address.");
        return;
      } else if (data.status === 500 || data.status === 400) {
        NotificationUtil.error("Something went wrong.");
        return;
      }
      NotificationUtil.success(
        "Verification code is sent to your email address."
      );
      setCodeInputVisible(true);
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const onFinish = (data: any) => {
    data["email"] = data["email"].toLowerCase();
    emailMutation.mutate(data.email);
    setEmail(data.email);
  };

  return (
    <Form layout="vertical" onFinish={onFinish} size="large">
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
        <Button disabled={codeInputVisible} type="primary" htmlType="submit">
          Send Code
        </Button>
      </Form.Item>
    </Form>
  );
};

export default SendVerificationCodeForm;
