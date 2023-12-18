import React, { useState } from "react";
import styles from "./Notification.module.scss";
import { useNavigate } from "react-router-dom";
import { getPost } from "../../Services/forum";

function Notification({ props }: { props: any }) {
  const navigate = useNavigate();

  const [isHovered, setIsHovered] = useState(false);
  const handleMouseOver = () => {
    setIsHovered(true);
  };
  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  async function handleClick() {
    if (props.parentType !== "ACHIEVEMENT") {
      if(props.parentType === "POST"){
        const post = await getPost(props.parent);
        navigate(`/forum/detail/${post.forumId}/${props.parent}`);
      }else if(props.parentType === "COMMENT"){
        const post = await getPost(props.parent);
        navigate(`/forum/detail/${post.forumId}/${props.parent}`);
      }
    } 
  }

  return (
    <div>
      <div
        className={`${props.isRead ? styles.notification : ""} ${isHovered ? styles.hovered : ""}`}
        onMouseOver={handleMouseOver}
        onMouseLeave={handleMouseLeave}
        onClick={handleClick}
      >
    
        <div className={styles.row}>
          <h3
            className={styles.notificationTitle} 
          >
            {props.message}
          </h3>
          <h3
            className={styles.floatingDescription}
          >
            {props.message}
          </h3>
        </div>

      </div>
    </div>
  );
}

export default Notification;
