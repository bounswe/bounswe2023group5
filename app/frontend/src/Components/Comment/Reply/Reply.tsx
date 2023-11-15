import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./reply.module.scss";
import { ArrowDownOutlined, ArrowUpOutlined, DeleteOutlined } from "@ant-design/icons";
import { useVote } from "../../Hooks/useVote";
import { Button } from "antd";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation } from "react-query";
import { deleteComment } from "../../../Services/comment";
import { useQueryClient } from "react-query";



function Reply({ reply }: { reply: any}) {

  const { upvote, downvote } = useVote({
    voteType: "REPLY",
    typeId: reply.id,
    invalidateKey: ["replies", reply.id],
  });

  const { user } = useAuth();


  const queryClient = useQueryClient();
  const { mutate: removereply } = useMutation(
    (id: string) => deleteComment(id),
    {
      onSuccess() {
        queryClient.invalidateQueries(["replies", reply.id]);
      },
      onMutate(id: string) {
        queryClient.setQueriesData(["replies", reply.postId], (prev: any) => {
          return prev?.filter((reply: any) => id !== reply.id);
        });
      },
    }
  );



  return (
    <div className={styles.container}>
      <div className={styles.vote}>
        <button type="button" onClick={upvote}>
          <ArrowUpOutlined />
        </button>
        <div>{reply.overallVote}</div>
        <button type="button" onClick={downvote}>
          <ArrowDownOutlined />
        </button>
      </div>
      <div className={styles.title}>{reply.replyContent}</div>
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
                  onClick={() => {removereply(reply.id)}}
                />
          </div>)}
      </div>
 
    </div>
  );
}

export default Reply;
