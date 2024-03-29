import React, { useState } from "react";
import styles from "./Achievement.module.scss";
import { useNavigate } from "react-router-dom";

function Achievement({ props }: { props: any }) {
  const navigate = useNavigate();

  const [isHovered, setIsHovered] = useState(false);
  const handleMouseOver = () => {
    setIsHovered(true);
  };
  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  function handleClick() {
    if (props.onClick) {
      props.onClick();
    } else {
      if (!props.game) {
        return;
      }
      const gameId = props.game;
      navigate(`/game/detail/${gameId}`);
    }
  }

  return (
    <div>
      <div
        className={`${styles.achievement} ${isHovered ? styles.hovered : ""}`}
        onMouseOver={handleMouseOver}
        onMouseLeave={handleMouseLeave}
        onClick={handleClick}
      >
        <img
          src={`${import.meta.env.VITE_APP_IMG_URL}${props?.icon}`}
          alt="achievement icon"
          width={120}
          height={100}
          className={styles.icon}
        ></img>

        <div className={styles.row}>
          <h3 className={styles.achievementTitle}>{props.title}</h3>
          <p className={styles.floatingDescription}>{props.description}</p>
        </div>
      </div>
    </div>
  );
}

export default Achievement;
