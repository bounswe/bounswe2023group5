import { useLocation, useNavigate, useParams } from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import { getCommentList } from "../../Services/comment";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";
import Comment from "../../Components/Comment/Comment/Comment.tsx";

import { useVote } from "../../Components/Hooks/useVote.tsx";
import { useAuth } from "../../Components/Hooks/useAuth.tsx";
import {
  DownOutlined,
  EditOutlined,
  UpOutlined,
  CommentOutlined,
  WarningOutlined,
} from "@ant-design/icons";
import clsx from "clsx";
import { Button } from "antd";
import { useState } from "react";
import TagRenderer from "../../Components/TagRenderer/TagRenderer.tsx";
import { twj } from "tw-to-css";

function ForumPost() {
  const { isLoggedIn, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const { postId, forumId } = useParams();
  const { data: post, isLoading } = useQuery(["post", postId], () =>
    getPost(postId!)
  );
  const { upvote, downvote } = useVote({
    voteType: "POST",
    typeId: postId!,
    invalidateKeys: [
      ["forum", forumId],
      ["post", postId],
    ],
  });
  const { data: comments, isLoading: isLoadingComments } = useQuery(
    ["comments", postId],
    () => getCommentList({ postId: postId! })
  );

  const [isCommenting, setCommenting] = useState(false);

  const toggleCommenting = () => {
    setCommenting(!isCommenting);
    console.log(isCommenting);
    console.log(post);
  };

  return (
    <div className={styles.container}>
      {!isLoading && (
        <div className={styles.postContainer}>
          {user?.id === post.poster.id && (
            <div className={styles.edit}>
              <Button
                onClick={() =>
                  navigate(
                    `/forum/form?forumId=${forumId}&&redirect=${location.pathname}&&editId=${postId}`
                  )
                }
              >
                <EditOutlined />
              </Button>
            </div>
          )}
          <div className={styles.title}>
            <div className={styles.vote}>
              <Button
                type="primary"
                shape="circle"
                icon={<UpOutlined />}
                size="small"
                onClick={upvote}
                disabled={!isLoggedIn}
                className={clsx(post?.userVote === "UPVOTE" && styles.active)}
              />
              <div>{post.overallVote}</div>

              <Button
                type="primary"
                shape="circle"
                size="small"
                icon={<DownOutlined />}
                onClick={downvote}
                disabled={!isLoggedIn}
                className={clsx(post?.userVote === "DOWNVOTE" && styles.active)}
              />
            </div>

            <h2>{post.title}</h2>
            <TagRenderer tags={post.tags} />
          </div>{" "}
          {post.postImage && (
            <div className={styles.image}>
              <img
                height="30px"
                src={`${import.meta.env.VITE_APP_IMG_URL}${post.postImage}`}
              />
            </div>
          )}
          <span className={styles.body}>{post.postContent}</span>
          <div style={twj("flex gap-2 pt-2")}>
            <Button
              size="small"
              icon={<CommentOutlined style={{ color: "#555064" }} />}
              onClick={() => {
                toggleCommenting();
              }}
            >
              Comment{" "}
            </Button>
            <WarningOutlined
              style={twj("text-red-500 text-lg cursor-pointer")}
              type="text"
              alt="report"
            />
          </div>
          {isCommenting && <CommentForm />}
        </div>
      )}

      <div className={styles.commentTitle}>Comments</div>
      {!isLoadingComments &&
        comments.map(
          (comment: any) =>
            !comment.isDeleted && (
              <Comment comment={comment} postId={postId!} key={comment.id} />
            )
        )}
    </div>
  );
}

export default ForumPost;
