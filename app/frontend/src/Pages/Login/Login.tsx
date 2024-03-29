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
import { useAuth } from "../../Components/Hooks/useAuth";
import postLogin from "../../Services/Login";
import { NotificationUtil } from "../../Library/utils/notification";
import { handleAxiosError } from "../../Library/utils/handleError";

function Login() {
  const { setUser, setToken } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const loginMutation = useMutation(postLogin, {
    onSuccess: async (data) => {
      if (data.status === 404) {
        NotificationUtil.error("Please enter a valid email address.");
        return;
      } else if (data.status === 500) {
        NotificationUtil.error("Something went wrong.");
        return;
      }

      const responseData: { token: string; user: any } = data.data;

      setUser(responseData.user);
      setToken(responseData.token);
      NotificationUtil.success("You successfully logged in");

      navigate("/");
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    loginMutation.mutate({ email, password });
  };

  return (
    <div className={styles.outerContainer}>
      <div className={styles.innerContainer}>
        <div className={styles.infoContainer}>
          <img
            className={styles.image}
            src="../../../assets/images/guru.jpeg"
          ></img>
          <h5 className={styles.loginInfo}>
            Join the Fun - Login and Conquer the Game World!
          </h5>
        </div>
        <div className={styles.formContainer}>
          <h2 className={styles.loginHeader}>Login</h2>

          <div className={styles.loginForm}>
            <form onSubmit={(event) => handleLogin(event)}>
              <Input
                size="large"
                placeholder="Please enter your Email"
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
                <Button
                  type="link"
                  onClick={() => navigate("/forgotpassword")}
                  className={styles.navigationLink}
                >
                  Forgot Password
                </Button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
