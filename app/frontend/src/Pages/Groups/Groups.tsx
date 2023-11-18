import { Select } from "antd";
import PrivateGroup from "../../Components/Groups/PrivateGroup";
import PublicGroup from "../../Components/Groups/PublicGroup";
import styles from "./Groups.module.scss";
import { useQuery } from "react-query";
import { getGames } from "../../Services/games";
import Search from "antd/es/input/Search";
import { useState } from "react";

function Groups() {
  const [searchText, setSearchText] = useState("");

  const membershipOptions = [
    { value: "private", label: "Private" },
    { value: "public", label: "Public" },
    { value: null, label: "All" },
  ];

  const { data: games } = useQuery(["games"], () => getGames());
  const gameOptions = games?.map((game: any) => {
    return { value: game.gameName, label: game.gameName };
  });

  const handleChange = (value: string) => {
    console.log(`selected ${value}`);
  };

  return (
    <div className={styles.groupsPage}>
      <div className={styles.groupsContainer}>
        <div className={styles.findGroups}>
          <Search
            placeholder="Search groups by name"
            enterButton
            className={styles.search}
            onSearch={setSearchText}
            style={{ width: "50%" }}
          />
          <Select
            placeholder="Filter by membership rule"
            defaultValue={null}
            options={membershipOptions}
            onChange={handleChange}
            style={{ width: "20%" }}
          />
          <Select
            mode="multiple"
            placeholder="Filter by game"
            onChange={handleChange}
            style={{ width: "30%" }}
            options={gameOptions}
          />
        </div>
        <PublicGroup></PublicGroup>
        <PrivateGroup></PrivateGroup>
      </div>
    </div>
  );
}

export default Groups;
