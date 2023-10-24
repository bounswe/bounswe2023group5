import styles from "./HelloWorld.module.scss";
import { Button } from "antd";
import { useNavigate } from "react-router-dom";


function HelloWorld() {
  const navigate = useNavigate();
  const Logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    localStorage.removeItem("username");
    navigate("/login")
  }
  return (
  <div style={{ alignContent: "center"}}>
  <div className={styles.hello}>Hello World!</div>
      <Button
      type="primary"
      danger={true}
      onClick={() => {
          Logout();
        }
      }
    >
      Logout
    </Button>
</div>);
}

export default HelloWorld;
