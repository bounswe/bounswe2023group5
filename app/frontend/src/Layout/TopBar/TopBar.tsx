import { Menu, MenuProps } from "antd";
import styles from "./TopBar.module.scss";
import {
  HomeTwoTone,
  UserOutlined,
  TeamOutlined,
  LoginOutlined,
  LogoutOutlined,
  FormOutlined,
} from "@ant-design/icons";
import GameController from "../../Components/Icons/GameController";
import { useAuth } from "../../Components/Hooks/useAuth";
import { useNavigate } from "react-router-dom";

function TopBar() {
  const { isLoggedIn, logOut, user } = useAuth();
  const navigate = useNavigate();
  const isAdmin = user?.role === "ADMIN";

  const itemsNotLoggedIn: MenuProps["items"] = [
    {
      key: "home",
      label: "Home",
      icon: <HomeTwoTone />,
    },
    {
      key: "games",
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
      key: "home",
      label: "Home",
      icon: <HomeTwoTone />,
    },
    {
      key: "games",
      label: "Games",
      icon: <GameController />,
    },
    {
      key: "groups",
      label: "Groups",
      icon: <TeamOutlined />,
    },
    {
      key: "profile",
      label: "Profile",
      icon: <UserOutlined />,
    },
    isAdmin
      ? {
          key: "admin",
          label: "Admin",
          icon: <FormOutlined />,
        }
      : null,
    {
      key: "logout",
      label: "Logout",
      icon: <LogoutOutlined />,
    },
  ];

  return (
    <div className={styles.container}>
      <div className={styles.logo}>
        <img height="30px" src="../../../assets/images/guru-logo.png"></img>Game
        Guru
      </div>
      <Menu
        className={styles.menu}
        mode="horizontal"
        defaultSelectedKeys={["games"]}
        items={isLoggedIn ? itemsLoggedIn : itemsNotLoggedIn}
        theme="dark"
        onClick={({ key }) => {
          key === "home"
            ? navigate("/")
            : key === "logout"
            ? logOut()
            : navigate(`${key}`);
        }}
      />
    </div>
  );
}

export default TopBar;
