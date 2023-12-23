import { useQuery } from "react-query";
import styles from "./HomePage.module.scss";
import { useAuth } from "../../Components/Hooks/useAuth";
import { getHomePosts } from "../../Services/home";
import ForumPost from "../../Components/Forum/ForumPost/ForumPost";
import { useState } from "react";
import { Button, Pagination, Select } from "antd";
import {
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
const sortOptions = [
  { label: "Creation Date", value: "CREATION_DATE" },
  { label: "Overall Vote", value: "OVERALL_VOTE" },
  { label: "Vote Count", value: "VOTE_COUNT" },
];
function HomePage() {
  const { user } = useAuth();
  const [sortBy, setSortBy] = useState<string>(sortOptions[0].value);
  const [sortDirection, setSortDir] = useState<"ASCENDING" | "DESCENDING">(
    "DESCENDING"
  );
  const toggleSortDir = () => {
    setSortDir((currentSortDir) =>
      currentSortDir === "ASCENDING" ? "DESCENDING" : "ASCENDING"
    );
  };
  const { data, isLoading } = useQuery(["home", user?.id], () =>
    getHomePosts(sortDirection, sortBy as any)
  );

  return (
    <div className={styles.pageContainer}>
      <div className={styles.postsContainer}>
        <div className={styles.filterContainer}>
          <Button onClick={toggleSortDir}>
            {sortDirection === "DESCENDING" ? (
              <SortDescendingOutlined />
            ) : (
              <SortAscendingOutlined />
            )}
          </Button>
          <Select
            options={sortOptions}
            value={sortBy}
            onChange={setSortBy}
            style={{ width: "120px" }}
          />
        </div>
        {data?.map((post: any) => (
          <div className={styles.postContainer}>
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
          </div>
        ))}
        <Pagination defaultCurrent={1} total={50} />;
      </div>
    </div>
  );
}

export default HomePage;
