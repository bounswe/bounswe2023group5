import { useQuery } from "react-query";
import styles from "./HomePage.module.scss";
import { useAuth } from "../../Components/Hooks/useAuth";
import { getHomePosts } from "../../Services/home";
import ForumPost from "../../Components/Forum/ForumPost/ForumPost";

function HomePage() {
  const { user } = useAuth();
  const { data, isLoading } = useQuery(["home", user?.id], getHomePosts);
  return (
    <div className={styles.pageContainer}>
      <div className={styles.postsContainer}>
        {data?.map((post: any) => (
          <ForumPost
            post={post}
            forumId={post.forum}
            gameId={post.type === "GAME" && post.typeId}
            redirect="/home"
            type={post.type}
            typeName={post.typeName}
            typeId={post.typeId}
            key={post.id}
          />
        ))}
      </div>
    </div>
  );
}

export default HomePage;
