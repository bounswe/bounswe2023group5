import SideBar from "../SideBar/SideBar";
import styles from "./MainLayout.module.scss";
import { Outlet } from "react-router-dom";

function MainLayout() {
  return (
    <div className={styles.holyGrailLayout}>
      <div className={styles.header}>Header</div>
      <div className={styles.leftColumn}>
        <SideBar></SideBar>
      </div>
      <div className={styles.mainContent}>
        <Outlet />
      </div>
      <div className={styles.rightColumn}>Right Sidebar</div>
      <div className={styles.footer}>Footer</div>
    </div>
  );
}

export default MainLayout;
