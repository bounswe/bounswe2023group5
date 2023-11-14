import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./Comment.module.scss";
import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import { useVote } from "../../Hooks/useVote";

function Comment({ comment, postId }: { comment: any; postId: string }) {
  const { upvote, downvote } = useVote({
    voteType: "COMMENT",
    typeId: comment.id,
    invalidateKey: ["post", postId],
  });
  return (
    <div className={styles.container}>
      <div className={styles.vote}>
        <button type="button" onClick={upvote}>
          <ArrowUpOutlined />
        </button>
        <div>{comment.overallVote}</div>
        <button type="button" onClick={downvote}>
          <ArrowDownOutlined />
        </button>
      </div>
      <div className={styles.title}>{comment.commentContent}</div>
      <div className={styles.meta}>
        <span>{comment.commenter.username}</span>
        <span>{comment.createdAt && formatDate(comment.createdAt)}</span>
      </div>
    </div>
  );
}

export default Comment;
