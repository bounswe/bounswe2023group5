import { Button } from "antd";
import styles from "./Main.module.scss";
import { useNavigate } from "react-router-dom";

function Main() {
  const navigate = useNavigate();
  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Welcome Admin!</h1>

      <div>
        <h3 className={styles.itemHeader}>1) Game</h3>
        <ul>
          <li>
            <Button type="link" onClick={() => navigate("/create-game")}>
              Create Game
            </Button>
          </li>
        </ul>
      </div>
      <div>
        <h3 className={styles.itemHeader}>2) Tag</h3>
        <ul>
          <li>
            <Button type="link" onClick={() => navigate("/create-tag")}>
              Create Tag
            </Button>
          </li>
          <li>
            <Button type="link" onClick={() => navigate("/update-tag")}>
              Update Tag
            </Button>
          </li>
          <li>
            <Button type="link" onClick={() => navigate("/delete-tag")}>
              Delete Tag
            </Button>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default Main;
