import { Menu, MenuProps } from "antd";
import styles from "./TopBar.module.scss";
import { HomeTwoTone, UserOutlined, TeamOutlined } from "@ant-design/icons";
import GameController from "../../Components/Icons/GameController";

const items: MenuProps["items"] = [
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
    <div className={styles.container}>
      <div className={styles.logo}>Logo</div>
      <Menu
        className={styles.menu}
        mode="horizontal"
        defaultSelectedKeys={["2"]}
        items={items}
        theme="dark"
      />
    </div>
  );
}

export default TopBar;