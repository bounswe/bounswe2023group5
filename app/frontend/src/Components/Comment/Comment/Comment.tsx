import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./Comment.module.scss";

import {
  CommentOutlined,
  DeleteOutlined,
  DownOutlined,
  EditOutlined,
  UpOutlined,
  WarningOutlined,
} from "@ant-design/icons";

import { useVote } from "../../Hooks/useVote";
import { Button } from "antd";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation } from "react-query";
import { deleteComment } from "../../../Services/comment";
import { useQueryClient } from "react-query";

import { useState } from "react";
import ReplyForm from "../ReplyForm/ReplyForm";
import Reply from "../Reply/Reply";
import CommentEditForm from "../CommentForm/CommentEditForm";
import { twj } from "tw-to-css";
import { NotificationUtil } from "../../../Library/utils/notification";
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
        NotificationUtil.success("You successfully delete the comment.");
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
            className={clsx(comment?.userVote === "UPVOTE" && styles.active)}
          />
          <div className={styles.title}>{comment.overallVote}</div>

          <Button
            type="primary"
            shape="circle"
            size="small"
            icon={<DownOutlined />}
            onClick={downvote}
            disabled={!isLoggedIn}
            className={clsx(comment?.userVote === "DOWNVOTE" && styles.active)}
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
            onClick={() => {
              toggleCommenting();
            }}
          />
          <WarningOutlined
            style={twj("text-red-500 text-lg cursor-pointer")}
            type="text"
            alt="report"
          />
          {(user?.username === comment.commenter.username || user?.isAdmin) && (
            <div className={styles.delete}>
              <Button
                type="text"
                ghost={true}
                shape="default"
                size="small"
                className={styles.delete}
                onClick={() => {
                  removeComment(comment.id);
                }}
              >
                <DeleteOutlined style={{ color: "red" }} />
              </Button>
            </div>
          )}
          {user?.id === comment.commenter.id && (
            <div className={styles.edit}>
              <Button onClick={() => toggleEditing()}>
                <EditOutlined />
              </Button>
            </div>
          )}
        </div>
      </div>
      {isCommenting && (
        <div>
          {isLoggedIn && <ReplyForm commentId={comment.id} />}
          {comment.replies.map(
            (reply: any) =>
              !reply.isDeleted && <Reply reply={reply} key={reply.id} />
          )}
        </div>
      )}

      {isEditing && (
        <div>
          {isLoggedIn && (
            <CommentEditForm
              commentId={comment.id}
              commentContent={comment.commentContent}
            />
          )}
        </div>
      )}
    </div>
  );
}

export default Comment;
