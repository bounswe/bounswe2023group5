import React, { useState } from "react";
import styles from "./Achievement.module.scss";

function Achievement({ props }: { props: any }) {
  const [isHovered, setIsHovered] = useState(false);
  const handleMouseOver = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  return (
    <div>
      <div
        className={`${styles.achievement} ${isHovered ? styles.hovered : ""}`}
        onMouseOver={handleMouseOver}
        onMouseLeave={handleMouseLeave}
      >
        <img
          src={`${import.meta.env.VITE_APP_IMG_URL}${props?.icon}`}
          alt="achievement icon"
          width={120}
          height={100}
          className={styles.icon}
        ></img>

        <div className={styles.achievement_content}>
          <div className={styles.row}>
            <h3
              className={`${styles.achievementTitle} ${
                isHovered ? styles.hovered : ""
              }`}
            >
              {props.title}
            </h3>
            {props.game && (
              <h3
                className={`${styles.achievementGame} ${
                  isHovered ? styles.hovered : ""
                }`}
              >
                {props.game.gameName}
              </h3>
            )}
            <p
              className={`${styles.floatingDescription} ${
                isHovered ? styles.hovered : ""
              }`}
            >
              {props.description}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Achievement;
