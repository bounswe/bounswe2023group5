import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./Comment.module.scss";
import { ArrowDownOutlined, ArrowUpOutlined, CommentOutlined, DeleteOutlined } from "@ant-design/icons";
import { useVote } from "../../Hooks/useVote";
import { Button, Form, Input } from "antd";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation } from "react-query";
import { deleteComment } from "../../../Services/comment";
import { useQueryClient } from "react-query";
import { useState } from "react";
import ReplyForm from "../ReplyForm/ReplyForm";
import Reply from "../Reply/Reply";



function Comment({ comment, postId }: { comment: any; postId: string }) {

  const { upvote, downvote } = useVote({
    voteType: "COMMENT",
    typeId: comment.id,
    invalidateKey: ["comments", postId],
  });

  const { user } = useAuth();

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
  const [isCommenting, setCommenting] = useState(false);

  const toggleCommenting = () => {
    setCommenting(!isCommenting);
    console.log(isCommenting);
  };




  return (
    <div className={styles.container}>
      <div className={styles.row}>
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
      </div>
      <div className={styles.row}>
        <div className={styles.meta}>
          <span>{comment.commenter.username}</span>
          <span>{comment.createdAt && formatDate(comment.createdAt)}</span>
          
          <Button
            type="text"
            ghost={true}
            shape="circle"
            size="small"
            icon={<CommentOutlined style={{ color: "red" }} />}
            onClick={() => {toggleCommenting()}}
          />
          

          {user.username === comment.commenter.username && (
            <div className={styles.delete}>
                <Button
                    type="text"
                    ghost={true}
                    shape="circle"
                    size="small"
                    icon={<DeleteOutlined style={{ color: "red" }} />}
                    onClick={() => {removeComment(comment.id)}}
                  />
            </div>)}
            
      </div>
    </div>
      {isCommenting && (
              <div>
                <ReplyForm  commentId={comment.id}/>
                {comment.replies.map((reply:any) => (
                  !reply.isDeleted &&
                  <Reply reply={reply} key={reply.id}/>
                ))}
              </div>
          )}
 
    </div>
  );
}

export default Comment;
