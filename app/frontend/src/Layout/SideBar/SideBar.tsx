import React, { useState } from "react";
import {
  TeamOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Button, Menu } from "antd";
import styles from "./SideBar.module.scss";
import Profile from "../../Components/Icons/Profile";
import { clsx } from "clsx";

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
  createItem("My Games", "sub1", <></>, [
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
    <div className={clsx(styles.container, collapsed && styles.collapsed)}>
      <Button type="primary" onClick={() => setCollapsed((c) => !c)}>
        {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
      </Button>

      <div className={styles.profilePic}>
        <Profile />
      </div>

      <Menu
        defaultSelectedKeys={["1"]}
        defaultOpenKeys={["sub1"]}
        mode="inline"
        inlineCollapsed={collapsed}
        items={items}
      />
    </div>
  );
}

export default SideBar;
