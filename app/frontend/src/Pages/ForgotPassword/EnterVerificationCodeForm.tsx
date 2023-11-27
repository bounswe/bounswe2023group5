// EnterVerificationCodeForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { useMutation } from "react-query";
import { postCode, postEmail } from "../../Services/ForgotPassword";
import { useAuth } from "../../Components/Hooks/useAuth";
import { NotificationUtil } from "../../Library/utils/notification";
import { handleAxiosError } from "../../Library/utils/handleError";

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
        NotificationUtil.error("Please enter a valid code");
        return;
      } else if (data.status === 500) {
        NotificationUtil.error("Something went wrong.");
        return;
      }

      const token: string = data.data;
      setToken(token);

      setIsVerified(true);
    },
    onError: (error) => {
      handleAxiosError(error);
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
        NotificationUtil.error("Please enter a valid email address.");
        return;
      } else if (data.status === 500 || data.status === 400) {
        NotificationUtil.error("Something went wrong.");
        return;
      }
      NotificationUtil.success(
        "Verification code is sent to your email address."
      );
    },
    onError: (error) => {
      handleAxiosError(error);
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
        <Button type="primary" onClick={onResend}>
          Resend Code
        </Button>
        <Button type="primary" htmlType="submit">
          Verify
        </Button>
      </div>
    </Form>
  );
};

export default EnterVerificationCodeForm;
