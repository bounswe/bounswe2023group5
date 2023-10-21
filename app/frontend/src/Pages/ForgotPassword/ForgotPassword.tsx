import { Button, Form, Input, Space, Card, message } from "antd";
import { MailOutlined } from "@ant-design/icons";
import { useState } from "react";
import styles from "./ForgotPassword.module.scss";

const ForgotPassword = () => {
  const [form] = Form.useForm();
  const [codeInputVisible, setCodeInputVisible] = useState(false);
  const [isVerified, setIsVerified] = useState(false);

  const onFinish = () => {
    message.success("Submit success!");
  };

  const onFinishFailed = () => {
    message.error("Submit failed!");
  };

  return (
    <Card title="Forgot Password" className={styles.form}>
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        size="large"
      >
        <Space.Compact block size="large">
          <Form.Item
            style={{ width: "100%" }}
            name="email"
            rules={[
              { required: true, message: "" },
              { type: "email", message: "Please provide a valid email" },
            ]}
          >
            <Input
              prefix={<MailOutlined className="site-form-item-icon" />}
              placeholder="Email"
            />
          </Form.Item>
          <Button
            disabled={codeInputVisible}
            type="primary"
            onClick={() => setCodeInputVisible(true)}
          >
            Send Code
          </Button>
        </Space.Compact>
        <Form.Item
          hidden={!codeInputVisible}
          name="verification-code"
          rules={[{ required: true, message: "" }]}
        >
          <Input placeholder="Verification Code" />
        </Form.Item>
        <div
          style={{ display: "flex", justifyContent: "flex-end ", gap: "10px" }}
        >
          <Form.Item hidden={!codeInputVisible || isVerified}>
            <Button type="primary">Resend Code</Button>
          </Form.Item>
          <Form.Item hidden={!codeInputVisible || isVerified}>
            <Button type="primary" onClick={() => setIsVerified(true)}>
              Verify
            </Button>
          </Form.Item>
        </div>
        <Form.Item
          name="password"
          rules={[{ required: true, message: "" }]}
          hidden={!isVerified}
        >
          <Input.Password placeholder="Enter a new password" />
        </Form.Item>
        <Form.Item
          name="conf_password"
          hidden={!isVerified}
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
                  new Error("The new password that you entered do not match!")
                );
              },
            }),
          ]}
        >
          <Input.Password placeholder="Confirm the new password" />
        </Form.Item>
        <Form.Item
          hidden={!isVerified}
          style={{ display: "flex", justifyContent: "flex-end" }}
        >
          <Button type="primary">Submit</Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default ForgotPassword;
