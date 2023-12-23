import { useQuery } from "react-query";
import styles from "./HomePage.module.scss";
import { useAuth } from "../../Components/Hooks/useAuth";
import { getHomePosts } from "../../Services/home";
import ForumPost from "../../Components/Forum/ForumPost/ForumPost";
import { useState } from "react";
import { Button, Carousel, Pagination, PaginationProps, Select } from "antd";
import {
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
import {
  getGameRecommendations,
  getGroupRecommendations,
} from "../../Services/recommendation";
const sortOptions = [
  { label: "Creation Date", value: "CREATION_DATE" },
  { label: "Overall Vote", value: "OVERALL_VOTE" },
  { label: "Vote Count", value: "VOTE_COUNT" },
];
function HomePage() {
  const { user, isLoading: userLoading } = useAuth();
  const [sortBy, setSortBy] = useState<string>(sortOptions[0].value);
  const [sortDirection, setSortDir] = useState<"ASCENDING" | "DESCENDING">(
    "DESCENDING"
  );
  const [pageData, setPageData] = useState<any[]>([]);

  const toggleSortDir = () => {
    setSortDir((currentSortDir) =>
      currentSortDir === "ASCENDING" ? "DESCENDING" : "ASCENDING"
    );
  };
  const { data, isLoading } = useQuery(
    ["home", user?.id],
    () => getHomePosts(sortDirection, sortBy as any),
    {
      enabled: !userLoading,
    }
  );

  const { data: games } = useQuery(
    ["gamerec", user?.id],
    getGameRecommendations,
    {
      enabled: !userLoading,
    }
  );
  const { data: groups } = useQuery(
    ["gamerec", user?.id],
    getGroupRecommendations,
    {
      enabled: !userLoading,
    }
  );

  const handleChange = (page: any, pageSize: any) => {
    setPageData(data?.slice((page - 1) * pageSize, page * pageSize));
  };

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
        <div></div>
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
        <Pagination
          onChange={handleChange}
          defaultCurrent={1}
          total={20}
          pageSize={5}
        />
      </div>
    </div>
  );
}

export default HomePage;
