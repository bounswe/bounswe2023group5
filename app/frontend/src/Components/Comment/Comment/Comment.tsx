import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./Comment.module.scss";
import {
  ArrowDownOutlined,
  ArrowUpOutlined,
  DeleteOutlined,
  DownOutlined,
  UpOutlined,
} from "@ant-design/icons";
import { useVote } from "../../Hooks/useVote";
import { Button } from "antd";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation } from "react-query";
import { deleteComment } from "../../../Services/comment";
import { useQueryClient } from "react-query";
import clsx from "clsx";

function Comment({ comment, postId }: { comment: any; postId: string }) {
  const { upvote, downvote } = useVote({
    voteType: "COMMENT",
    typeId: comment.id,
    invalidateKey: ["comments", postId],
  });

  const { user, isLoggedIn } = useAuth();

  const queryClient = useQueryClient();
  const { mutate: removeComment } = useMutation(
    (id: string) => deleteComment(id),
    {
      onSuccess() {
        queryClient.invalidateQueries(["comments", postId]);
      },
      onMutate(id: string) {
        queryClient.setQueriesData(["comments", postId], (prev: any) => {
          return prev?.filter((comment: any) => id !== comment.id);
        });
      },
    }
  );

  return (
    <div className={styles.container}>
      <div className={styles.vote}>
        <Button
          type="primary"
          shape="circle"
          size="small"
          icon={<UpOutlined />}
          onClick={upvote}
          disabled={!isLoggedIn}
          //className={clsx(post?.userVote === "UPVOTE" && styles.active)}
        />
        <div>{comment.overallVote}</div>

        <Button
          type="primary"
          shape="circle"
          size="small"
          icon={<DownOutlined />}
          onClick={downvote}
          disabled={!isLoggedIn}
          //className={clsx(post?.userVote === "DOWNVOTE" && styles.active)}
        />
      </div>
      <div className={styles.title}>{comment.commentContent}</div>
      <div className={styles.meta}>
        <span>{comment.commenter.username}</span>
        <span>{comment.createdAt && formatDate(comment.createdAt)}</span>
        {user.username === comment.commenter.username && (
          <div className={styles.delete}>
            <Button
              type="text"
              ghost={true}
              shape="circle"
              size="small"
              icon={<DeleteOutlined style={{ color: "red" }} />}
              onClick={() => {
                removeComment(comment.id);
              }}
            />
          </div>
        )}
      </div>
    </div>
  );
}

export default Comment;
