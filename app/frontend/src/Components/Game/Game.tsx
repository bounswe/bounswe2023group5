import styles from "./Game.module.scss";
import GameConsole from "../../../assets/images/game-console.png";
import { Button } from "antd";
import { useNavigate } from "react-router-dom";

function Game(props: any) {
  const game = props.game;

  const navigate = useNavigate();
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <img
          src={GameConsole}
          alt="game-console"
          width={40}
          height={40}
          className={styles.icon}
        ></img>
        <h4>{game.gameName}</h4>
      </div>
      <div className={styles.content}>
        <img
          src={`${import.meta.env.VITE_APP_IMG_URL}${game?.gameIcon}`}
          alt="name of the game"
          width={120}
          height={100}
          className={styles.icon}
        ></img>
        <div className={styles.descriptionContainer}>
          <p className={styles.description}>
            {game.gameDescription.length > 104
              ? `${game.gameDescription.substring(0, 104)}...`
              : game.gameDescription}
          </p>
          <Button
            onClick={() => navigate(`/game/${game.id}`)}
            className={styles.button}
          >
            Details
          </Button>
        </div>
      </div>
    </div>
  );
}

export default Game;
