import { useParams } from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import { getCommentList } from "../../Services/comment";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";
import Comment from "../../Components/Comment/Comment/Comment.tsx";
import { useVote } from "../../Components/Hooks/useVote.tsx";
import { useAuth } from "../../Components/Hooks/useAuth.tsx";
import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import clsx from "clsx";

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
              <button
                type="button"
                onClick={upvote}
                disabled={!isLoggedIn}
                className={clsx(post?.userVote === "UPVOTE" && styles.active)}
              >
                <ArrowUpOutlined />
              </button>
              <div>{post.overallVote}</div>
              <button
                type="button"
                onClick={downvote}
                disabled={!isLoggedIn}
                className={clsx(post?.userVote === "DOWNVOTE" && styles.active)}
              >
                <ArrowDownOutlined />
              </button>
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
