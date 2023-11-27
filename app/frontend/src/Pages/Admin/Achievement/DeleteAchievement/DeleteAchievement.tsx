import { useState } from "react";
import { useMutation, useQuery } from "react-query";
import styles from "./DeleteAchievement.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input } from "antd";
import { deleteAchievementByName } from "../../../../Services/achievement";
import { getGames } from "../../../../Services/games";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { handleError } from "../../../../Library/utils/handleError";
import { NotificationUtil } from "../../../../Library/utils/notification";

function DeleteAchievement() {
  const [title, setTitle] = useState("");
  const [game, setGame] = useState("");

  const deleteAchievementMutation = useMutation(deleteAchievementByName, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully delete the achievement.");
    },
    onError: (error) => {
      handleError(error);
    },
  });

  const { data: games } = useQuery(["games"], () => getGames());

  const handleClick = async () => {
    deleteAchievementMutation.mutate({
      title,
      game,
    });
  };

  const onChange = (_filterKey: string, value: string) => {
    setGame(value);
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
        {games?.length > 0 && (
          <SingleSelect
            className={styles.select}
            title="Game"
            elements={games.map((game) => game.gameName)}
            onChange={onChange}
            reset={false}
          ></SingleSelect>
        )}
        <br></br>
        <Button className={styles.filterButton} onClick={handleClick}>
          Delete Achievement
        </Button>
      </div>
    </div>
  );
}

export default DeleteAchievement;
