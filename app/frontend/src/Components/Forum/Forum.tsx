import { useNavigate } from "react-router-dom";
import styles from "./Forum.module.scss";
import { useQuery } from "react-query";
import { getPostList } from "../../Services/forum";
import { Button, Input, Select } from "antd";
import {
  PlusCircleOutlined,
  SearchOutlined,
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
import { useAuth } from "../Hooks/useAuth";
import ForumPost from "./ForumPost/ForumPost";
import { useState } from "react";
import { useDebounce } from "usehooks-ts";
const sortOptions = [
  { label: "Creation Date", value: "CREATION_DATE" },
  { label: "Edit Date", value: "EDIT_DATE" },
  { label: "Overall Vote", value: "OVERALL_VOTE" },
  { label: "Vote Count", value: "VOTE_COUNT" },
];

function Forum({
  forumId,
  redirect = "/",
}: {
  forumId: string;
  redirect?: string;
}) {
  const { isLoggedIn } = useAuth();
  const [sortBy, setSortBy] = useState<string>(sortOptions[0].value);
  const [sortDir, setSortDir] = useState<"ASCENDING" | "DESCENDING">(
    "DESCENDING"
  );
  const [search, setSearch] = useState<string>("");
  const searchDebounced = useDebounce(search, 500);
  const searchString =
    searchDebounced.length >= 3 ? searchDebounced : undefined;
  const toggleSortDir = () => {
    setSortDir((currentSortDir) =>
      currentSortDir === "ASCENDING" ? "DESCENDING" : "ASCENDING"
    );
  };

  const { data: posts } = useQuery(
    ["forum", forumId, sortBy, sortDir, searchString],
    () =>
      getPostList({
        forum: forumId,
        sortBy,
        sortDirection: sortDir,
        search: searchString,
      })
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
        <div className={styles.searchContainer}>
          <span>
            <Input
              placeholder="Search..."
              prefix={<SearchOutlined />}
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </span>
          <Button onClick={toggleSortDir}>
            {sortDir === "DESCENDING" ? (
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
        {posts?.map((post: any) => (
          <ForumPost key={post.id} post={post} forumId={forumId} />
        ))}
      </div>
    </div>
  );
}

export default Forum;
