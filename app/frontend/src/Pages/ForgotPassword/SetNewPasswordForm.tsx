// SetNewPasswordForm.tsx
import React from "react";
import { Form, Input, Button } from "antd";
import { useMutation } from "react-query";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface SetNewPasswordFormProps {
  isVerified: boolean;
}

const SetNewPasswordForm: React.FC<SetNewPasswordFormProps> = ({ isVerified }) => {


  const navigate = useNavigate();

  const postPassword = async (newPassword:any) => {
    const authorizationHeader = axios.defaults.headers.common['Authorization'];
    const headers:any = {
      'Content-Type': 'application/json',
    };
  
    if (authorizationHeader) {
      headers['Authorization'] = authorizationHeader;
    }

    return fetch(import.meta.env.VITE_APP_API_URL + "/api/auth/change-forgot-password", {
      method: "POST",
      headers:headers,
      body: JSON.stringify(newPassword)
      });
  }
  const passwordMutation = useMutation(postPassword,{  
    onSuccess: (data) => {
      if(data.status === 500){
        alert("Something went wrong.")
        return;
      }
      alert("Password is successfully set")
      navigate("/login");
    },
    onError: () => {
  
    },});


  const onFinish = (data:any) => {
    // Logic for setting a new password
    passwordMutation.mutate(data)
  };

  return (
    <Form
      onFinish={onFinish}
      size="large"
    >
      <Form.Item
        name="newPassword"
        rules={[{ required: true, message: "Please enter a new password" }]
      }>
        <Input.Password placeholder="Enter a new password" />
      </Form.Item>
      <Form.Item
        name="conf_password"
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
