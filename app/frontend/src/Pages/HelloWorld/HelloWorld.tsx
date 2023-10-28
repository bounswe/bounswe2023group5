import { Button } from "antd";
import styles from "./HelloWorld.module.scss";

function HelloWorld() {
  return (
    <div className={styles.hello}>
      <Button type="primary">Hello World</Button>
    </div>
  );
}

export default HelloWorld;
