import { useState } from "react";
import { useMutation, useQuery } from "react-query";
import styles from "./DeleteGame.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button } from "antd";
import { deleteGame, getGames } from "../../../../Services/games";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { handleError } from "../../../../Library/utils/handleError";
import { NotificationUtil } from "../../../../Library/utils/notification";

function DeleteGame() {
  const [gameName, setGameName] = useState("");

  const deleteGameMutation = useMutation(deleteGame, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully delete the game.");
    },
    onError: (error) => {
      handleError(error);
    },
  });

  const { data: games } = useQuery(["games"], () => getGames());

  const handleClick = async () => {
    const game = games.find((game) => game.gameName === gameName);

    deleteGameMutation.mutate(game.id);
  };

  const onChange = (_filterKey: string, value: string) => {
    setGameName(value);
  };
  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Delete Game</h1>

      <div className={styles.form}>
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
          Delete Game
        </Button>
      </div>
    </div>
  );
}

export default DeleteGame;
