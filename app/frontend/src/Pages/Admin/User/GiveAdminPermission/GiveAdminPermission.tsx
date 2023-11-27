import { Button, Input } from "antd";
import styles from "./GiveAdminPermission.module.scss";
import { useState } from "react";
import { useMutation } from "react-query";
import { giveAdminPermission } from "../../../../Services/user";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleError } from "../../../../Library/utils/handleError";

function GiveAdminPermission() {
  const [username, setUsername] = useState("");

  const giveAdminPermissionMutation = useMutation(giveAdminPermission, {
    onSuccess: async () => {
      NotificationUtil.success(
        `You successfully give admin permission to username ${username}.`
      );
    },
    onError: (error) => {
      handleError(error);
    },
  });

  const handleClick = () => {
    giveAdminPermissionMutation.mutate(username);
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Give Admin Permission to User</h1>

      <div className={styles.form}>
        <Input
          placeholder="Username"
          value={username}
          className={styles.input}
          onChange={(event) => setUsername(event.target.value)}
        />

        <Button type="primary" className={styles.button} onClick={handleClick}>
          Give Admin Permission
        </Button>
      </div>
    </div>
  );
}

export default GiveAdminPermission;
