import { Menu, MenuProps } from "antd";
import { Header } from "antd/es/layout/layout";
import styles from "./TopBar.module.scss";
import { HomeTwoTone, UserOutlined, TeamOutlined } from "@ant-design/icons";
import GameController from "../../Components/Icons/GameController";

const items1: MenuProps["items"] = [
  {
    key: "nav-home",
    label: "Home",
    icon: <HomeTwoTone />,
  },
  {
    key: "nav-games",
    label: "Games",
    icon: <GameController />,
  },
  {
    key: "nav-groups",
    label: "Groups",
    icon: <TeamOutlined />,
  },
  {
    key: "nav-profile",
    label: "Profile",
    icon: <UserOutlined />,
  },
];

function TopBar() {
  return (
    <div>
      <div className={styles.platform}>GamerInsight</div>
      <Menu
        className={styles.menu}
        theme="dark"
        mode="horizontal"
        items={items1}
      />
    </div>
  );
}

export default TopBar;
