import { useNavigate, useParams } from "react-router-dom";
import styles from "./Forum.module.scss";
import { useQuery } from "react-query";
import { getPostList } from "../../Services/forum";
import { Button } from "antd";
import { PlusCircleOutlined } from "@ant-design/icons";
import { useAuth } from "../Hooks/useAuth";
import ForumPost from "./ForumPost/ForumPost";

function Forum({
  forumId,
  redirect = "/",
}: {
  forumId: string;
  redirect?: string;
}) {
  const { isLoggedIn } = useAuth();
  const { data: posts, isLoading } = useQuery(["forum", forumId], () =>
    getPostList({ forum: forumId })
  );
  const navigate = useNavigate();
  return (
    <div className={styles.container}>
      <div className={styles.top}>
        {isLoggedIn && (
          <Button
            onClick={() =>
              navigate(`/forum/form?forumId=${forumId}&&redirect=${redirect}`)
            }
          >
            <PlusCircleOutlined /> Add Post
          </Button>
        )}
      </div>
      <div className={styles.posts}>
        {posts?.map((post: any) => (
          <ForumPost key={post.id} post={post} forumId={forumId} />
        ))}
      </div>
    </div>
  );
}

export default Forum;
