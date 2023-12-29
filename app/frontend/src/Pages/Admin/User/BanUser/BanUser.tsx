import { Button, Input } from "antd";
import styles from "./BanUser.module.scss";
import { useState } from "react";
import { useMutation } from "react-query";
import { banUser } from "../../../../Services/user";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleAxiosError } from "../../../../Library/utils/handleError";

function BanUser() {
  const [username, setUsername] = useState("");

  const banUserMutation = useMutation(banUser, {
    onSuccess: async () => {
      NotificationUtil.success(
        `You successfully banned the user with username ${username}.`
      );
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const handleClick = () => {
    banUserMutation.mutate(username);
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Ban User</h1>

      <div className={styles.form}>
        <Input
          placeholder="Username"
          value={username}
          className={styles.input}
          onChange={(event) => setUsername(event.target.value)}
        />

        <Button type="primary" className={styles.button} onClick={handleClick}>
          Ban User
        </Button>
      </div>
    </div>
  );
}

export default BanUser;
