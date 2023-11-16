import { useParams } from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import {getCommentList} from "../../Services/comment";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";
import Comment from "../../Components/Comment/Comment/Comment.tsx";

function ForumPost() {
  const { postId } = useParams();
  const { data: post, isLoading } = useQuery(["post", postId], () =>
    getPost(postId!)
  );
  const { data: comments, isLoading: isLoadingComments } = useQuery(["comments", postId], () =>
    getCommentList({postId:postId!})
  );
  return (
    <div className={styles.container}>
      {!isLoading && (
          <div className={styles.postContainer}>
            <span className={styles.title}>{post.title}</span>
            <span>{post.postContent}</span>
          </div>
      )}

      
      <div className={styles.title}>Comments</div>
      {!isLoadingComments && (comments.map((comment:any) => ( 
        !comment.isDeleted &&
        <Comment comment={comment} postId={postId!} key={comment.id}/>
      )))}

    <CommentForm/>

    </div>
  );
}

export default ForumPost;
