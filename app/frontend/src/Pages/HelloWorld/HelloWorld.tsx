import { Button } from "antd";
import styles from "./HelloWorld.module.scss";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../Components/Hooks/useAuth";

function HelloWorld() {
  const navigate = useNavigate();
  const { user, logOut } = useAuth();
  return (
    <div style={{ alignContent: "center" }}>
      <div className={styles.hello}>Hello World!</div>
      <Button type="primary" danger={true} onClick={logOut}>
        Logout
      </Button>
      <pre>{JSON.stringify(user, undefined, 2)}</pre>
    </div>
  );
}

export default HelloWorld;
