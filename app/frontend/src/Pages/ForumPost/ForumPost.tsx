import { useParams } from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import {getCommentList} from "../../Services/comment";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";
import Comment from "../../Components/Comment/Comment/Comment.tsx";
import { Button } from "antd";
import { CommentOutlined } from "@ant-design/icons";
import { useState } from "react";
import { formatDate } from "../../Library/utils/formatDate.ts";

function ForumPost() {
  const { postId } = useParams();
  const { data: post, isLoading } = useQuery(["post", postId], () =>
    getPost(postId!)
  );
  const { data: comments, isLoading: isLoadingComments } = useQuery(["comments", postId], () =>
    getCommentList({postId:postId!})
  );

  const [isCommenting, setCommenting] = useState(false);


  const toggleCommenting = () => {
    setCommenting(!isCommenting);
    console.log(isCommenting);
    console.log(post)
  };

  return (
    <div className={styles.container}>
      {!isLoading && (
          <div className={styles.postContainer}>
            <span className={styles.title}>{post.title}</span>
            <span>{post.postContent}</span>
            <div className={styles.meta}>
              <span>{post.poster.username}</span>
              <span>{post.createdAt && formatDate(post.createdAt)}</span>
              <Button
                type="text"
                ghost={true}
                shape="circle"
                size="small"
                icon={<CommentOutlined style={{ color: "red"}} />}
                style={{marginLeft:"5em" , marginTop:"15px"}}
                onClick={() => {toggleCommenting()}}
              />
            </div>

          {isCommenting && <CommentForm/>}

          </div>
      )}

      
      <div className={styles.title}>Comments</div>
      {!isLoadingComments && (comments.map((comment:any) => ( 
        !comment.isDeleted &&
        <Comment comment={comment} postId={postId!} key={comment.id}/>
      )))}

    

    </div>
  );
}

export default ForumPost;
