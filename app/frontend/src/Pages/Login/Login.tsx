import React, { useState } from "react";
import styles from "./Login.module.css";
import { useNavigate } from "react-router-dom";
import {
  UserOutlined,
  LockOutlined,
  EyeTwoTone,
  EyeInvisibleOutlined,
} from "@ant-design/icons";
import { Button, Input } from "antd";
import { useMutation } from "react-query";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();
  const postLogin = async (formData:any) => {
    return fetch(import.meta.env.VITE_APP_API_URL + "/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });
  }
  const loginMutation = useMutation(postLogin,{  
    onSuccess: async( data) => {
      if(data.status === 404){
        alert("Please enter a valid email address.")
        return;
      }else if(data.status === 500){
        alert("Something went wrong.")
        return;
      }
      console.log(data.status)
      const responseData: { token: string, user:any} = await data.json();
      const jwtToken:string = responseData.token;
      const user:any = responseData.user;
      localStorage.setItem("token", jwtToken);
      localStorage.setItem("email", user.email);
      localStorage.setItem("role", user.role);
      localStorage.setItem("username", user.username);
      if(localStorage.getItem("token"))
        console.log("Token is set");
        navigate("/hello");
    },
    onError: () => {
      alert("Your email or password is incorrect.")
    },});

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    loginMutation.mutate({ email, password });
  };

  return (
    <div className={styles.outerContainer}>
      <div className={styles.innerContainer}>
        <h2 className={styles.loginHeader}>Login</h2>
        <h5 className={styles.loginInfo}>
          Join the Fun - Login and Conquer the Game World!
        </h5>
        <div className={styles.loginForm}>
          <form onSubmit={(event) => handleLogin(event)}>
            <Input
              size="large"
              placeholder="Username or Email"
              className={styles.formElem}
              prefix={
                <UserOutlined
                  style={{ fontSize: "1.2rem", marginRight: "0.4rem" }}
                />
              }
              value={email}
              onChange={(event) => setEmail(event.target.value)}
            />

            <Input.Password
              placeholder="Password"
              prefix={
                <LockOutlined
                  style={{ fontSize: "1.2rem", marginRight: "0.4rem" }}
                />
              }
              iconRender={(visible) =>
                visible ? (
                  <EyeTwoTone style={{ fontSize: "1rem" }} />
                ) : (
                  <EyeInvisibleOutlined style={{ fontSize: "1rem" }} />
                )
              }
              className={styles.formElem}
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />

            <div className={styles.buttonContainer}>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
              >
                Login
              </Button>
              <Button
                type="link"
                onClick={() => navigate("/register")}
                className={styles.navigationLink}
              >
                Don't you have an account? Register
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
