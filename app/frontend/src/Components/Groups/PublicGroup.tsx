import { TeamOutlined, UserOutlined } from "@ant-design/icons";
import styles from "./PublicGroup.module.scss";
import { Button } from "antd";

function PublicGroup() {
  return (
    <div className={styles.group}>
      <div className={styles.header}>
        <div style={{ display: "flex", alignItems: "center" }}>
          <TeamOutlined />
          <div>
            <b>heder mi keder mi</b>
          </div>
        </div>
      </div>
      <div className={styles.body}>
        <div className={styles.imgContainer}>
          <img src="../../../assets/images/guru.jpeg"></img>
        </div>
        <div className={styles.content}>
          <div className={styles.description}>
            <span>
              Here is some description for people who like to enjoy
              minecraftiiie
            </span>
          </div>
          <div className={styles.footer}>
            <div>
              <UserOutlined></UserOutlined> 45 members
            </div>
            <Button>Join</Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PublicGroup;
