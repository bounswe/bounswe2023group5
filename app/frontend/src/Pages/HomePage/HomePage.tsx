import { useQuery } from "react-query";
import styles from "./HomePage.module.scss";
import { useAuth } from "../../Components/Hooks/useAuth";
import { getHomePosts } from "../../Services/home";
import ForumPost from "../../Components/Forum/ForumPost/ForumPost";
import { useRef, useState } from "react";
import { Button, Carousel, Pagination, Select } from "antd";
import {
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
import {
  getGameRecommendations,
  getGroupRecommendations,
} from "../../Services/recommendation";
import GameReccomendation from "../../Components/ReccomendationCarousel/RecommendationItem/RecommendationItem";
import { useElementSize } from "usehooks-ts";
import RecommendationCarousel from "../../Components/ReccomendationCarousel/RecommendationCarousel";
import { PacmanLoader } from "react-spinners";
const sortOptions = [
  { label: "Creation Date", value: "CREATION_DATE" },
  { label: "Overall Vote", value: "OVERALL_VOTE" },
  { label: "Vote Count", value: "VOTE_COUNT" },
];

const PAGE_SIZE = 5;
function HomePage() {
  const { user, isLoading: userLoading } = useAuth();
  const [sortBy, setSortBy] = useState<string>(sortOptions[0].value);
  const [sortDirection, setSortDir] = useState<"ASCENDING" | "DESCENDING">(
    "DESCENDING"
  );
  const [page, setPage] = useState(1);

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
    ["grouprec", user?.id],
    getGroupRecommendations,
    {
      enabled: !userLoading,
    }
  );

  return (
    <div className={styles.pageContainer}>
      <div className={styles.postsContainer}>
        <RecommendationCarousel
          title="Recommended Games!"
          items={games?.map((game: any) => ({
            image: game.gameIcon,
            name: game.gameName,
            link: `/game/detail/${game.id}`,
          }))}
          loading={!games}
        />
        <RecommendationCarousel
          title="Recommended Groups!"
          items={groups?.map((group: any) => ({
            image: group.groupIcon,
            name: group.title,
            link: `/group/detail/${group.id}`,
            showName: true,
          }))}
          loading={!groups}
        />
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
            style={{ width: "200px" }}
          />
        </div>

        {data ? (
          data
            ?.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE)
            .map((post: any) => (
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
            ))
        ) : (
          <div className={styles.spinnerContainer}>
            <PacmanLoader color="#1b4559" size={30} />
          </div>
        )}
        <Pagination
          style={{ alignSelf: "flex-end" }}
          onChange={setPage}
          current={page}
          total={data?.length}
          pageSize={PAGE_SIZE}
        />
      </div>
    </div>
  );
}

export default HomePage;
