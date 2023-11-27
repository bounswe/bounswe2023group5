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
import { useNavigate } from "react-router-dom";

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

function SideBar() {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();

  const { user, isLoggedIn, profile } = useAuth();

  const items: MenuItem[] = [
    createItem(
      "My Games",
      "game",
      <AppstoreOutlined />,
      profile?.games
        .slice(0, 5)
        .map((game: any) => createItem(game.gameName, game.id))
    ),

    createItem(
      "My Groups",
      "group",
      <TeamOutlined />,
      profile?.groups
        .slice(0, 5)
        .map((group: any) => createItem(group.title, group.id))
    ),
  ];
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
          {!isLoggedIn ? (
            <img src="../../../assets/images/guru.jpeg"></img>
          ) : profile && profile.profilePhoto ? (
            <img src={profile.profilePhoto}></img>
          ) : (
            <ProfileIcon />
          )}
        </div>
        {!collapsed && isLoggedIn && <div>{user.username}</div>}
        {!collapsed && !isLoggedIn && <div>Game Guru</div>}
        {isLoggedIn && (
          <Menu
            defaultOpenKeys={["my-games"]}
            mode="inline"
            inlineCollapsed={collapsed}
            items={items}
            onClick={({ keyPath }) =>
              navigate(`${keyPath[1]}/detail/${keyPath[0]}`)
            }
          />
        )}
      </div>
    </ConfigProvider>
  );
}

export default SideBar;
