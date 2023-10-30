import { Menu, MenuProps, Typography } from "antd";
import styles from "./TopBar.module.scss";
import {
  HomeTwoTone,
  UserOutlined,
  TeamOutlined,
  LoginOutlined,
} from "@ant-design/icons";
import GameController from "../../Components/Icons/GameController";
import { useAuth } from "../../Components/Hooks/useAuth";
import { useNavigate } from "react-router-dom";

const { Title } = Typography;

function TopBar() {
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const itemsNotLoggedIn: MenuProps["items"] = [
    {
      key: "Home",
      label: "Home",
      icon: <HomeTwoTone />,
    },
    {
      key: "Games",
      label: "Games",
      icon: <GameController />,
    },
    {
      key: "Groups",
      label: "Groups",
      icon: <TeamOutlined />,
    },
    {
      key: "main-login",
      label: "Login",
      icon: <LoginOutlined />,
      children: [
        {
          key: "login",
          label: "Login",
        },
        {
          key: "register",
          label: "Register",
        },
      ],
    },
  ];

  const itemsLoggedIn: MenuProps["items"] = [
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

  return (
    <div className={styles.container}>
      <div className={styles.logo}>Game Guru</div>
      <Menu
        className={styles.menu}
        mode="horizontal"
        defaultSelectedKeys={["Home"]}
        items={isLoggedIn ? itemsLoggedIn : itemsNotLoggedIn}
        theme="dark"
        onClick={({ key }) => {
          key !== "Home" ? navigate(`${key}`) : navigate("/");
        }}
      />
    </div>
  );
}

export default TopBar;
