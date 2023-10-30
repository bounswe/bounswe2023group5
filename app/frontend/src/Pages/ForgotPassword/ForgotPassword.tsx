import React, { useState } from "react";
import { Card } from "antd";
import SendVerificationCodeForm from "./SendVerificationCodeForm";
import EnterVerificationCodeForm from "./EnterVerificationCodeForm";
import SetNewPasswordForm from "./SetNewPasswordForm";
import styles from "./ForgotPassword.module.scss";

const ForgotPassword: React.FC = () => {
  const [codeInputVisible, setCodeInputVisible] = useState(false);
  const [isVerified, setIsVerified] = useState(false);
  const [email, setEmail] = useState("");

  return (
    <Card title="Forgot Password" className={styles.form}>
      {!codeInputVisible && !isVerified && (
        <SendVerificationCodeForm
          codeInputVisible={codeInputVisible}
          setCodeInputVisible={setCodeInputVisible}
          setEmail={setEmail}
        />
      )}

      {codeInputVisible && !isVerified && (
        <EnterVerificationCodeForm
          isVerified={isVerified}
          setIsVerified={setIsVerified}
          email={email}
        />
      )}

      {isVerified && <SetNewPasswordForm isVerified={isVerified} />}
    </Card>
  );
};

export default ForgotPassword;
