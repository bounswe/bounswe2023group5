import { Button, Input } from "antd";
import styles from "./DeleteTag.module.scss";
import { useState } from "react";
import { useMutation, useQuery } from "react-query";
import { deleteTag, getTags } from "../../../../Services/tags";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleAxiosError } from "../../../../Library/utils/handleError";

function DeleteTag() {
  const [name, setName] = useState("");

  const { data: tags } = useQuery(["tags"], getTags);

  const deleteTagMutation = useMutation(deleteTag, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully delete the tag.");
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const handleClick = () => {
    const tag = tags.find((tag) => tag.name === name);
    if (!tag) {
      NotificationUtil.error("Tag does not exists");
      return;
    }
    deleteTagMutation.mutate(tag.id);
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Delete Tag</h1>

      <div className={styles.form}>
        <Input
          placeholder="Name"
          value={name}
          className={styles.input}
          onChange={(event) => setName(event.target.value)}
        />

        <Button type="primary" className={styles.button} onClick={handleClick}>
          Delete Tag
        </Button>
      </div>
    </div>
  );
}

export default DeleteTag;
