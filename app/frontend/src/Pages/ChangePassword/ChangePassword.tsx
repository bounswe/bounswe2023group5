import { Button, Form, Input, Card, message } from "antd";
import styles from "./ChangePassword.module.scss";
import { changePassword } from "../../Services/changepassword";
import { useNavigate } from "react-router-dom";

const ChangePassword = () => {
  const [form] = Form.useForm();
  const navigate = useNavigate();

  const handleChangePassword = async (event: any) => {
    const { currentPassword, newPassword } = event;
    try {
      const response = await changePassword(currentPassword, newPassword);
      if (response?.status == 200) {
        message.success("Password is changed successfully!");
        navigate("/profile");
      }
    } catch (error: any) {
      message.error(error.message);
      return;
    }

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
        <Form.Item
          name="currentPassword"
          rules={[{ required: true, message: "" }]}
        >
          <Input placeholder="Password" />
        </Form.Item>

        <Form.Item name="newPassword" rules={[{ required: true, message: "" }]}>
          <Input.Password placeholder="Enter a new password" />
        </Form.Item>
        <Form.Item
          name="conf-password"
          dependencies={["newPassword"]}
          rules={[
            {
              required: true,
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("newPassword") === value) {
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
