import { PacmanLoader } from "react-spinners";
import { useAuth } from "../../Components/Hooks/useAuth";
import styles from "./Profile.module.scss";
import { useQueryClient } from "react-query";
import { Popover } from "antd";
import { useState } from "react";
const subPages = ["Activities", "Games", "Eklenir Daha"];
function Profile() {
  const { user, isLoading } = useAuth();
  const [subPage, setSubPage] = useState(subPages[0]);
  return (
    <div className={styles.profilePage}>
      {isLoading ? (
        <div className={styles.profileError}>
          <PacmanLoader color="#1b4559" size={30} />
        </div>
      ) : (
        <>
          <div className={styles.profileCard}>
            <div className={styles.profilePicture}>
              <img src="../../../assets/images/guru.jpeg" />
            </div>
            <div className={styles.profileDetails}>
              <div className={styles.profileName}>
                <h1>{user.username}</h1>
                <span>{user.email}</span>
              </div>
              <div className={styles.socialContainer}>
                <div style={{ backgroundColor: "#000000" }}>
                  <img src="/icons/steam.svg" />{" "}
                  <Popover content={"wtf"}>Steam Account</Popover>
                </div>
                <div style={{ backgroundColor: "#0071bc" }}>
                  <img src="/icons/epic_games.svg" />{" "}
                  <Popover content={"wtf"}>Epic Account</Popover>
                </div>
                <div style={{ backgroundColor: "#107c10" }}>
                  {" "}
                  <img src="/icons/xbox.svg" />
                  <Popover content={"wtf"}> Xbox Account</Popover>
                </div>
              </div>
            </div>
          </div>
          <div className={styles.profileMenu}>
            {subPages.map((item) => (
              <button>{item}</button>
            ))}
          </div>
          <div className={styles.profileContent}>{subPage}</div>
        </>
      )}
    </div>
  );
}

export default Profile;
