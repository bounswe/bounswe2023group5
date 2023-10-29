import React, { useRef, useState } from "react";
import {
  TeamOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  AppstoreOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { ConfigProvider, Menu } from "antd";
import styles from "./SideBar.module.scss";
import Profile from "../../Components/Icons/Profile";
import { clsx } from "clsx";
import { getThemeColor } from "../../Components/Providers/AntdConfigProvider";

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
          <Profile />
        </div>
        {!collapsed && <div>CiselTheBarbie</div>}
        <Menu
          defaultOpenKeys={["sub1"]}
          mode="inline"
          inlineCollapsed={collapsed}
          items={items}
        />
      </div>
    </ConfigProvider>
  );
}

export default SideBar;
