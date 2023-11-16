import { Button } from "antd";
import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./ForumPost.module.scss";
import { DeleteFilled } from "@ant-design/icons";
import { useMutation } from "react-query";
import { deletePost } from "../../../Services/forum";
import { useAuth } from "../../Hooks/useAuth";
import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import { useVote } from "../../Hooks/useVote";
import clsx from "clsx";

function ForumPost({ post, forumId }: { post: any; forumId: string }) {
  const { user, isLoggedIn } = useAuth();

  const isAdmin = user?.role === "ADMIN";
  const deletePostMutation = useMutation(deletePost, {
    onSuccess: async () => {
      alert("You successfully delete the post.");
    },
    onError: () => {
      alert("Something went wrong");
    },
  });

  const handleDelete = () => {
    deletePostMutation.mutate(post.id);
  };

  const { upvote, downvote } = useVote({
    voteType: "POST",
    typeId: post.id,
    invalidateKeys: [
      ["forum", forumId],
      ["post", post.id],
    ],
  });
  return (
    <div className={styles.container}>
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
      <div className={styles.titleContainer}>
        <div className={styles.title}>{post.title}</div>
        {isAdmin && (
          <DeleteFilled style={{ color: "red" }} onClick={handleDelete} />
        )}
      </div>
      <div className={styles.meta}>
        <span>{post.poster.username}</span>
        <span>{post.createdAt && formatDate(post.createdAt)}</span>
      </div>
      <div className={styles.readMore}>
        <Button href={`/forum/detail/${forumId}/${post.id}`}>Read More</Button>
      </div>
    </div>
  );
}

export default ForumPost;
