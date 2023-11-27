import React, { useState } from "react";
import styles from "./SquareAchievement.module.scss";
import { Tooltip } from "antd";
import { useNavigate } from "react-router-dom";

function SquareAchievement({ props, onClick }: { props: any, onClick?: any }) {


  const navigate = useNavigate();

  function handleClick() {
    if (onClick) {
      onClick();
    } else {
      if (!props.game) {
        return;
        
      }
      const gameId = props.game;
      navigate(`/game/detail/${gameId}`);
    }
  }

  return (
    <div className={styles.achievement} onClick={handleClick}>
      <div className={styles.achievement_content}>
        <Tooltip
          title={props.title + ": " + props.description}
          className={styles.tootip}
        >
          <img
            src={`${import.meta.env.VITE_APP_IMG_URL}${props?.icon}`}
            alt="achievement icon"
            className={styles.icon}
            onClick={handleClick}
          ></img>
        </Tooltip>
      </div>
    </div>
  );
}

export default SquareAchievement;
