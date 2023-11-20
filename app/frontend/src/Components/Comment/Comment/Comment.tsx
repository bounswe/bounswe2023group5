import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./Comment.module.scss";

import {
  ArrowDownOutlined,
  ArrowUpOutlined,
  CommentOutlined,
  DeleteOutlined,
  DownOutlined,
  EditOutlined,
  UpOutlined,
} from "@ant-design/icons";

import { useVote } from "../../Hooks/useVote";
import { Button, Form, Input } from "antd";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation } from "react-query";
import { deleteComment } from "../../../Services/comment";
import { useQueryClient } from "react-query";

import clsx from "clsx";
import { useState } from "react";
import ReplyForm from "../ReplyForm/ReplyForm";
import Reply from "../Reply/Reply";
import { useNavigate } from "react-router-dom";
import CommentForm from "../CommentForm/CommentForm";
import CommentEditForm from "../CommentForm/CommentEditForm";


function Comment({ comment, postId }: { comment: any; postId: string }) {
  const { upvote, downvote } = useVote({
    voteType: "COMMENT",
    typeId: comment.id,
    invalidateKey: ["comments", postId],
  });

  const { user, isLoggedIn } = useAuth();
  const navigate = useNavigate();
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
  const [isEditing, setEditing] = useState(false);

  const toggleCommenting = () => {
    setCommenting(!isCommenting);
  };

  const toggleEditing = () => {
    setEditing(!isEditing);
  };


  return (
    <div className={styles.container}>
      <div className={styles.row}>
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
        <div className={styles.title}>{comment.overallVote}</div>

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

        <div className={styles.text}>{comment.commentContent}</div>
        
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
            icon={<CommentOutlined style={{ color: "#555064" }} />}	            
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
              onClick={() => {
                removeComment(comment.id);
              }}
            />
          </div>
        )}
          {user.id === comment.commenter.id && (
            <div className={styles.edit}>
              <Button
                onClick={() =>
                  toggleEditing()
                }
              >
                <EditOutlined />
              </Button>
            </div>
          )}

          
      </div>
    </div>
      {isCommenting && (
              <div>
                {isLoggedIn &&
                (<ReplyForm  commentId={comment.id}/>)}
                {comment.replies.map((reply:any) => (
                  !reply.isDeleted &&
                  <Reply reply={reply} key={reply.id}/>
                ))}
              </div>
          )}

      {isEditing && (
              <div>
                {isLoggedIn &&
                (<CommentEditForm commentId={comment.id}  commentContent={comment.commentContent}/>)}
              </div>
          )}
 

    </div>
  );
}

export default Comment;
