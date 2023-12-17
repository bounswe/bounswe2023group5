import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./Reply.module.scss";
import { DeleteOutlined, DownOutlined, UpOutlined } from "@ant-design/icons";
import { useVote } from "../../Hooks/useVote";
import { Button } from "antd";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation } from "react-query";
import { deleteComment } from "../../../Services/comment";
import { useQueryClient } from "react-query";
import { NotificationUtil } from "../../../Library/utils/notification";

function Reply({ reply }: { reply: any }) {
  const { upvote, downvote } = useVote({
    voteType: "COMMENT",
    typeId: reply.id,
    invalidateKey: ["comments", reply.post],
  });

  const { user, isLoggedIn } = useAuth();

  const queryClient = useQueryClient();
  const { mutate: removereply } = useMutation(
    (id: string) => deleteComment(id),
    {
      onSuccess() {
        queryClient.invalidateQueries(["comments", reply.post]);
        NotificationUtil.success("You successfully delete the comment.");
      },
      onMutate(id: string) {
        queryClient.setQueriesData(["comments", reply.post], (prev: any) => {
          return prev?.filter((reply: any) => id !== reply.id);
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
          onClick={downvote}
          disabled={!isLoggedIn}
          //className={clsx(post?.userVote === "DOWNVOTE" && styles.active)}
        />
        <div style={{ fontWeight: "bold" }}>{reply.overallVote}</div>
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
      <div className={styles.title}>{reply.commentContent}</div>
      <div className={styles.meta}>
        <span>{reply.commenter.username}</span>
        <span>{reply.createdAt && formatDate(reply.createdAt)}</span>
        {user.username === reply.commenter.username && (
          <div className={styles.delete}>
            <Button
              type="text"
              ghost={true}
              shape="circle"
              size="small"
              icon={<DeleteOutlined style={{ color: "red" }} />}
              onClick={() => {
                removereply(reply.id);
              }}
            />
          </div>
        )}
      </div>
    </div>
  );
}

export default Reply;
