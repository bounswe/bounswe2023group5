import SideBar from "../SideBar/SideBar";
import TopBar from "../TopBar/TopBar";
import styles from "./MainLayout.module.scss";
import { Outlet } from "react-router-dom";

function MainLayout() {
  return (
    <div className={styles.holyGrailLayout}>
      <div className={styles.header}>
        <TopBar />
      </div>

      <div className={styles.middle}>
        <SideBar />{" "}
        <div className={styles.mainContent}>
          <Outlet />
        </div>
      </div>
      <div className={styles.footer}>Footer</div>
    </div>
  );
}

export default MainLayout;
