import { Button, Form, Input, Space, Card, message } from "antd";
import { MailOutlined } from "@ant-design/icons";
import { useState } from "react";
import styles from "./ForgotPassword.module.scss";

const ForgotPassword = () => {
  const [form] = Form.useForm();
  const [codeInputVisible, setCodeInputVisible] = useState(false);

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
          <Button type="primary" onClick={() => setCodeInputVisible(true)}>
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

        <Form.Item name="password" rules={[{ required: true, message: "" }]}>
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
                  new Error("The new password that you entered do not match!")
                );
              },
            }),
          ]}
        >
          <Input.Password placeholder="Confirm the new password" />
        </Form.Item>

        <Button type="primary">Submit</Button>
      </Form>
    </Card>
  );
};

export default ForgotPassword;
