import React, { useState } from "react";
import styles from "./Register.module.css";
import {
  UserOutlined,
  LockOutlined,
  EyeTwoTone,
  MailOutlined,
  EyeInvisibleOutlined,
} from "@ant-design/icons";
import { Button, Checkbox, Input, Modal } from "antd";
import { useNavigate } from "react-router-dom";
import { useMutation } from "react-query";
import postRegister from "../../Services/Register";
import { CheckboxChangeEvent } from "antd/es/checkbox";
import { userAgreementText } from "./Register.const";
import { NotificationUtil } from "../../Library/utils/notification";
import { handleAxiosError } from "../../Library/utils/handleError";

function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [agreeTerm, setAgreeTerm] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const navigate = useNavigate();

  const registerMutation = useMutation(postRegister, {
    onSuccess: async (data) => {
      if (data.status === 500) {
        NotificationUtil.error("Something went wrong");
        return;
      }
      NotificationUtil.success("You registered successfully.");

      navigate("/login");
    },
    onError: (error: any) => {
      handleAxiosError(error);
    },
  });

  const handleRegister = (event: React.FormEvent<HTMLFormElement>) => {
    // check whether email is valid email address
    // check whether passwords are match
    if (email.indexOf("@") === -1) {
      NotificationUtil.error("Please enter a valid email address!");
    } else if (password !== confirmPassword) {
      NotificationUtil.error("Passwords do not match!");
    } else if (agreeTerm === false) {
      NotificationUtil.error("Please agree with our user agreement!");
    } else {
      registerMutation.mutate({ username, email, password });
    }
    event.preventDefault();
  };

  const handleAgreeTerm = (e: CheckboxChangeEvent) => {
    setAgreeTerm(e.target.checked);
  };

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
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
            <Checkbox
              onChange={handleAgreeTerm}
              checked={agreeTerm}
              className={styles.agreeCheckbox}
              disabled={isModalOpen}
            >
              I agree to the{" "}
              <span className={styles.userAgreementLink} onClick={showModal}>
                Game Guru User Agreement
              </span>
            </Checkbox>

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
      <Modal
        title="User Agreement"
        open={isModalOpen}
        footer={null}
        onCancel={handleCancel}
      >
        <p>{userAgreementText}</p>
      </Modal>
    </div>
  );
}

export default Register;
