import { useParams } from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";

function ForumPost() {
  const { postId } = useParams();
  const { data: post, isLoading } = useQuery(["post", postId], () =>
    getPost(postId!)
  );
  return (
    <div className={styles.container}>
      {!isLoading && (
        <div>
          <div className={styles.postContainer}>
            <span className={styles.title}>{post.title}</span>
            <span>{post.postContent}</span>
          </div>
          <CommentForm/>
        </div>
      )}
    </div>
  );
}

export default ForumPost;
