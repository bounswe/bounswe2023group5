import React, { useState } from "react";
import {
  TeamOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  AppstoreOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { ConfigProvider, Menu } from "antd";
import styles from "./SideBar.module.scss";
import ProfileIcon from "../../Components/Icons/ProfileIcon";
import { clsx } from "clsx";
import { getThemeColor } from "../../Components/Providers/AntdConfigProvider";
import { useAuth } from "../../Components/Hooks/useAuth";

type MenuItem = Required<MenuProps>["items"][number];

function createItem(
  label: React.ReactNode,
  key: React.Key,
  icon?: React.ReactNode,
  children?: MenuItem[],
  type?: "group"
): MenuItem {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as MenuItem;
}

const items: MenuItem[] = [
  createItem("My Games", "sub1", <AppstoreOutlined />, [
    createItem("Minecraft", "5"),
    createItem("Rounds", "6"),
    createItem("Dota", "7"),
    createItem("Stardoll", "8"),
  ]),

  createItem("My Groups", "sub2", <TeamOutlined />, [
    createItem("RoundsAndRounds", "9"),
    createItem("D0TA", "10"),
  ]),
];

function SideBar() {
  const [collapsed, setCollapsed] = useState(false);

  const { user, isLoggedIn } = useAuth();

  return (
    <ConfigProvider
      theme={{
        token: {
          colorBgContainer: getThemeColor("color-accent"),
        },
        components: {
          Menu: {
            activeBarBorderWidth: 0,
          },
        },
      }}
    >
      <div className={clsx(styles.container, collapsed && styles.collapsed)}>
        <button
          type="button"
          className={styles.toggleBtn}
          onClick={() => setCollapsed((c) => !c)}
        >
          {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
        </button>

        <div className={styles.profilePic}>
          {isLoggedIn ? (
            <ProfileIcon />
          ) : (
            <img src="../../../assets/images/guru.jpeg"></img>
          )}
        </div>
        {!collapsed && isLoggedIn && <div>{user.username}</div>}
        {!collapsed && !isLoggedIn && <div>Game Guru</div>}
        {isLoggedIn && (
          <Menu
            defaultOpenKeys={["sub1"]}
            mode="inline"
            inlineCollapsed={collapsed}
            items={items}
          />
        )}
      </div>
    </ConfigProvider>
  );
}

export default SideBar;
