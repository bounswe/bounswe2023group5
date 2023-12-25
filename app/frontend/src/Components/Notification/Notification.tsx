import styles from "./Notification.module.scss";
import { useNavigate } from "react-router-dom";
import { getPost } from "../../Services/forum";
import { formatDate } from "../../Library/utils/formatDate";
import clsx from "clsx";

function Notification({ props }: { props: any }) {
  const navigate = useNavigate();

  async function handleClick() {
    if (props.parentType !== "ACHIEVEMENT") {
      if (props.parentType === "POST") {
        const post = await getPost(props.parent);
        navigate(
          `/forum/detail/${post.forumId}/${props.parent}?back=/notifications`
        );
      } else if (props.parentType === "COMMENT") {
        const post = await getPost(props.parent);
        navigate(
          `/forum/detail/${post.forumId}/${props.parent}?back=/notifications`
        );
      } else if (props.parentType === "GROUP") {
        navigate(`/group/detail/${props.parent}`);
      } else if (props.parentType === "GROUP_APPLICATION") {
        navigate(`/group/review-application/${props.parent}`);
      }
    }
  }

  return (
    <div
      className={clsx(styles.notification, !props.isRead && styles.unread)}
      onClick={handleClick}
    >
      <p className={styles.notificationContent}>{props.message}</p>
      <p className={styles.date}>{formatDate(props.createdAt)}</p>
    </div>
  );
}

export default Notification;
