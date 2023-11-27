import { PacmanLoader } from "react-spinners";
import { useAuth } from "../../Components/Hooks/useAuth";
import styles from "./Profile.module.scss";
import { useQueryClient } from "react-query";
import { Popover } from "antd";
import { useState } from "react";
import EditProfile from "../../Components/Profile/EditProfile";
import ProfileIcon from "../../Components/Icons/ProfileIcon";
const subPages = ["Activities", "Games", "Eklenir Daha"];
function Profile() {
  const { user, isLoading, profile } = useAuth();
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
            <div className={styles.edit}>
              <EditProfile profile={profile} key={profile?.id} />
            </div>
            <div className={styles.profilePicture}>
              {profile.profilePhoto ? (
                <img
                  src={`${import.meta.env.VITE_APP_IMG_URL}${
                    profile.profilePhoto
                  }`}
                />
              ) : (
                <ProfileIcon />
              )}
            </div>
            <div className={styles.profileDetails}>
              <div className={styles.profileName}>
                <h1>{user.username}</h1>
                <span>{user.email}</span>
              </div>
              <div className={styles.socialContainer}>
                {!(
                  profile.steamProfile ||
                  profile.epicGamesProfile ||
                  profile.xboxProfileProfile
                ) && (
                  <span>
                    Add your game accounts here by editing your profile!
                  </span>
                )}
                {profile.steamProfile && (
                  <a
                    href={profile.steamProfile}
                    style={{ backgroundColor: "#000000" }}
                  >
                    <img src="/icons/steam.svg" />{" "}
                    <Popover
                      content={
                        <a href={profile.steamProfile}>
                          {profile.steamProfile}
                        </a>
                      }
                    >
                      Steam Account
                    </Popover>
                  </a>
                )}
                {profile.epicGamesProfile && (
                  <a
                    href={profile.epicGamesProfile}
                    style={{ backgroundColor: "#0071bc" }}
                  >
                    <img src="/icons/epic_games.svg" />
                    <Popover
                      content={
                        <a href={profile.epicGamesProfile}>
                          {profile.epicGamesProfile}
                        </a>
                      }
                    >
                      Epic Account
                    </Popover>
                  </a>
                )}
                {profile.xboxProfile && (
                  <a
                    href={profile.xboxProfile}
                    style={{ backgroundColor: "#107c10" }}
                  >
                    {" "}
                    <img src="/icons/xbox.svg" />
                    <Popover
                      content={
                        <a href={profile.xboxProfile}>{profile.xboxProfile}</a>
                      }
                    >
                      {" "}
                      Xbox Account
                    </Popover>
                  </a>
                )}
              </div>
            </div>
          </div>
          <div className={styles.profileMenu}>
            {subPages.map((item) => (
              <button onClick={() => setSubPage(item)}>{item}</button>
            ))}
          </div>
          <div className={styles.profileContent}>{subPage}</div>
        </>
      )}
    </div>
  );
}

export default Profile;
