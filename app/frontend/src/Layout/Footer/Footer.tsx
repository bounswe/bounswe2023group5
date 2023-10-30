import { Typography } from "antd";
import styles from "./Footer.module.scss";

function Footer() {
  return (
    <div className={styles.footer}>
      <Typography
        style={{ position: "absolute", right: "10px", bottom: "3px" }}
      >
        Designed by Ax3Ç
      </Typography>
    </div>
  );
}

export default Footer;
