// SetNewPasswordForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";

interface SetNewPasswordFormProps {
  isVerified: boolean;
}

const SetNewPasswordForm: React.FC<SetNewPasswordFormProps> = ({ isVerified }) => {
  const onFinish = () => {
    // Logic for setting a new password
    console.log("deneme")
  };

  return (
    <Form
      onFinish={onFinish}
      size="large"
    >
      <Form.Item
        name="password"
        rules={[{ required: true, message: "Please enter a new password" }]
      }>
        <Input.Password placeholder="Enter a new password" />
      </Form.Item>
      <Form.Item
        name="conf_password"
        dependencies={["password"]}
        rules={[
          {
            required: true,
          },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue("password") === value) {
                return Promise.resolve();
              }
              return Promise.reject(
                new Error("The new password that you entered does not match!")
              );
            },
          }),
        ]}
      >
        <Input.Password placeholder="Confirm the new password" />
      </Form.Item>
      <div style={{ display: isVerified ? "flex" : "none", justifyContent: "flex-end" }}>
        <Button type="primary" htmlType="submit">Change Password</Button>
      </div>
    </Form>
  );
};

export default SetNewPasswordForm;
