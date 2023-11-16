import { useParams } from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import { getCommentList } from "../../Services/comment";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";
import Comment from "../../Components/Comment/Comment/Comment.tsx";
import { useVote } from "../../Components/Hooks/useVote.tsx";
import { useAuth } from "../../Components/Hooks/useAuth.tsx";
import {
  ArrowDownOutlined,
  ArrowUpOutlined,
  DownOutlined,
  UpOutlined,
} from "@ant-design/icons";
import clsx from "clsx";
import { Button } from "antd";

function ForumPost() {
  const { isLoggedIn } = useAuth();
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
  return (
    <div className={styles.container}>
      {!isLoading && (
        <div className={styles.postContainer}>
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
            {post.title}
          </div>
          <span className={styles.body}>{post.postContent}</span>
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

      <CommentForm />
    </div>
  );
}

export default ForumPost;