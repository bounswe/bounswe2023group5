import { Button } from "antd";
import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./ForumPost.module.scss";

function ForumPost({ post }: { post: any }) {
  return (
    <div className={styles.container}>
      <div className={styles.title}>{post.title}</div>
      <div className={styles.meta}>
        <span>{post.poster.username}</span>
        <span>{post.createdAt && formatDate(post.createdAt)}</span>
      </div>
      <div className={styles.readMore}>
        <Button href={`/forum/detail/${post.id}`}>Read More</Button>
      </div>
    </div>
  );
}

export default ForumPost;
