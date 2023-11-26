import { Button, Select } from "antd";
import PrivateGroup from "../../Components/Groups/PrivateGroup";
import PublicGroup from "../../Components/Groups/PublicGroup";
import styles from "./Groups.module.scss";
import { useQuery } from "react-query";
import { getGames } from "../../Services/games";
import Search from "antd/es/input/Search";
import { useState } from "react";
import { getGroups } from "../../Services/groups";
import {
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
import { getTags } from "../../Services/tags";

function Groups() {
  const membershipOptions = [
    { value: "PRIVATE", label: "Private" },
    { value: "PUBLIC", label: "Public" },
    { value: null, label: "All" },
  ];

  const { data: games } = useQuery(["games"], () => getGames());
  const gameOptions = games?.map((game: any) => {
    return { value: game.gameName, label: game.gameName };
  });

  const sortOptions = [{ label: "Creation Date", value: "CREATION_DATE" }];

  const { data: tagOptions } = useQuery(["tagOptions", "groups"], async () => {
    const data = await getTags({ tagType: "GROUP" });
    return data.map((item: { name: any; id: any }) => ({
      label: item.name,
      value: item.id,
    }));
  });

  const [title, setTitle] = useState("");
  const [tags, setTags] = useState([]);
  const [membershipPolicy, setMembershipPolicy] = useState();
  const [gameName, setGameName] = useState("");
  const [sortBy, setSortBy] = useState("");
  const [sortDir, setSortDir] = useState<"ASCENDING" | "DESCENDING">(
    "DESCENDING"
  );

  console.log("tags here: ", tags);

  const { data: groups } = useQuery(
    ["groups", title, gameName, tags, membershipPolicy, sortBy, sortDir],
    () =>
      getGroups({ tags, title, gameName, membershipPolicy, sortBy, sortDir })
  );

  const toggleSortDir = () => {
    setSortDir((currentSortDir) =>
      currentSortDir === "ASCENDING" ? "DESCENDING" : "ASCENDING"
    );
  };

  return (
    <div className={styles.groupsPage}>
      <div className={styles.groupsContainer}>
        <div className={styles.findGroups}>
          <Search
            placeholder="Search groups by name"
            enterButton
            className={styles.search}
            onSearch={setTitle}
            onChange={(e) => {
              e.target.value === "" ? setTitle("") : "";
            }}
            style={{ width: "50%" }}
          />
          <Select
            placeholder="Filter by membership rule"
            defaultValue={null}
            options={membershipOptions}
            onChange={setMembershipPolicy}
            style={{ width: "15%" }}
          />
          <Button onClick={toggleSortDir}>
            {sortDir === "DESCENDING" ? (
              <SortDescendingOutlined />
            ) : (
              <SortAscendingOutlined />
            )}
          </Button>
          <Select
            options={sortOptions}
            defaultValue={sortBy}
            value={"CREATION_DATE"}
            onChange={setSortBy}
            style={{ flex: 1 }}
          />
          <Select
            placeholder="Filter by game"
            onChange={setGameName}
            style={{ width: "30%" }}
            options={gameOptions}
          />
          <Select
            mode="multiple"
            placeholder="Filter by tag"
            onChange={setTags}
            style={{ width: "30%" }}
            options={tagOptions}
          />
        </div>
        {groups &&
          groups.map((group: any) =>
            group.membershipPolicy === "PRIVATE" ? (
              <PrivateGroup key={group.id} group={group}></PrivateGroup>
            ) : (
              <PublicGroup key={group.id} group={group}></PublicGroup>
            )
          )}
      </div>
    </div>
  );
}

export default Groups;
