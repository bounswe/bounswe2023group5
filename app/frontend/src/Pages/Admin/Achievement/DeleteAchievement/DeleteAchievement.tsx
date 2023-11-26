import { useState } from "react";
import { useMutation } from "react-query";
import styles from "./DeleteAchievement.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input } from "antd";
import { deleteAchievementByName } from "../../../../Services/achievement";

function CreateAchievement() {
  const [title, setTitle] = useState("");
  const [game, setGame] = useState("");

  const deleteAchievementMutation = useMutation(deleteAchievementByName, {
    onSuccess: async () => {
      alert("You successfully delete the achievement.");
    },
    onError: () => {
      alert("Something went wrong");
    },
  });

  const handleClick = async () => {
    deleteAchievementMutation.mutate({
      title,
      game,
    });
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Delete Achievement</h1>

      <div className={styles.form}>
        <Input
          placeholder="Achievement Title"
          value={title}
          className={styles.input}
          onChange={(event) => setTitle(event.target.value)}
        />
        <Input
          placeholder="Game"
          value={game}
          className={styles.input}
          onChange={(event) => setGame(event.target.value)}
        />
        <Button className={styles.filterButton} onClick={handleClick}>
          Delete Achievement
        </Button>
      </div>
    </div>
  );
}

export default CreateAchievement;
