import { Button, Form, Input, Card, message } from "antd";
import styles from "./ChangePassword.module.scss";

const ChangePassword = () => {
  const [form] = Form.useForm();

  const handleChangePassword = () => {
    /*
    const values = form.getFieldsValue();
   
    const body = {
      email: values.email,
      new_password: values.new_password,
    };
    */
    message.success("Password is changed successfully!");
  };

  const onFinishFailed = () => {
    message.error("Couldn't change the password");
  };

  return (
    <Card title="Change Password" className={styles.form}>
      <Form
        form={form}
        layout="vertical"
        onFinish={handleChangePassword}
        onFinishFailed={onFinishFailed}
        size="large"
      >
        <Form.Item name="password" rules={[{ required: true, message: "" }]}>
          <Input placeholder="Password" />
        </Form.Item>

        <Form.Item name="newpassword" rules={[{ required: true, message: "" }]}>
          <Input.Password placeholder="Enter a new password" />
        </Form.Item>
        <Form.Item
          name="conf-password"
          dependencies={["newpassword"]}
          rules={[
            {
              required: true,
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("newpassword") === value) {
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
        <Form.Item style={{ display: "flex", justifyContent: "flex-end" }}>
          <Button htmlType="submit" type="primary">
            Change Password
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default ChangePassword;
