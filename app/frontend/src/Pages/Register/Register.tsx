import React, { useState } from "react";
import styles from "./Register.module.css";
import {
  UserOutlined,
  LockOutlined,
  EyeTwoTone,
  MailOutlined,
  EyeInvisibleOutlined,
} from "@ant-design/icons";
import { Button, Input } from "antd";
import { useNavigate } from "react-router-dom";
import { useMutation } from "react-query";


function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const navigate = useNavigate();

  const postLogin = async (formData:any) => {
    return fetch(import.meta.env.VITE_APP_API_URL + "/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });
  }
  const registerMutation = useMutation(postLogin,{  
    onSuccess: async(data) => {
      if(data.status === 500){
        alert("Something went wrong.")
        return;
      }
      console.log(data.status)
      alert("You registered successfully.")
      console.log("Token is set");
      navigate("/login");
    },
    onError: () => {
      alert("You cannot be registered.")
    },});
    


  const handleRegister = (event: React.FormEvent<HTMLFormElement>) => {
    // check whether email is valid email address
    // check whether passwords are match
    if (email.indexOf("@") === -1) {
      alert("Please enter a valid email address!");
    } else if(password !== confirmPassword) {
      alert("Passwords do not match!");
    } else {
      registerMutation.mutate({ username, email, password });
    }
    event.preventDefault();
  };

  return (
    <div className={styles.outerContainer}>
      <div className={styles.innerContainer}>
        <h2 className={styles.registerHeader}>Register</h2>
        <h5 className={styles.registerInfo}>
          Join the Adventure: Register Today!
        </h5>
        <div className={styles.registerForm}>
          <form onSubmit={(event) => handleRegister(event)}>
            <Input
              size="large"
              placeholder="Username"
              className={styles.formElem}
              prefix={
                <UserOutlined
                  style={{ fontSize: "1.2rem", marginRight: "0.4rem" }}
                />
              }
              value={username}
              onChange={(event) => setUsername(event.target.value)}
            />

            <Input
              size="large"
              placeholder="Email"
              className={styles.formElem}
              prefix={
                <MailOutlined
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

            <Input.Password
              placeholder="Confirm Password"
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
              value={confirmPassword}
              onChange={(event) => setConfirmPassword(event.target.value)}
            />

            <div className={styles.buttonContainer}>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
              >
                Register
              </Button>
              <Button
                type="link"
                className={styles.navigationLink}
                onClick={() => navigate("/login")}
              >
                Do you have an account? Login
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Register;
