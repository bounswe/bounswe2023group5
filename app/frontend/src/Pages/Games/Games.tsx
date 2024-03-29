import { useState } from "react";
import { useQuery } from "react-query";
import styles from "./Games.module.scss";
import PacmanLoader from "react-spinners/PacmanLoader";
import Game from "../../Components/Game/Game";
import SingleSelect from "../../Components/SingleSelect/SingleSelect";
import MultipleSelect from "../../Components/MultipleSelect/MultipleSelect";
import { Button, Input } from "antd";
import { getGames } from "../../Services/games";
import { getTags } from "../../Services/tags";
import PromotedEntities from "../../Components/PromotedEntities/PromotedEntities";

function Games() {
  const [filters, setFilters] = useState<any>({
    playerTypes: [],
    genre: [],
    production: "",
    platform: [],
    artStyle: [],
  });

  const { Search } = Input;
  const [searchText, setSearchText] = useState("");
  const [activeFilters, setActiveFilters] = useState();

  const { data: games } = useQuery(
    ["games", activeFilters, searchText],
    () =>
      getGames(activeFilters, searchText.length <= 0 ? undefined : searchText),

    {
      onSuccess: (data) => {
        // Check if the data is an array and has at least two elements
        if (Array.isArray(data) && data.length >= 2) {
          // Update promotedEntities with the first two elements of the data array
          setPromotedEntities(data.slice(0, 2));
          setNotPromotedGames(data.slice(2));
        }
      },
    }
  );
  const [promotedEntities, setPromotedEntities] = useState<any[]>([]);
  const [notPromotedgames, setNotPromotedGames] = useState<any[]>([]);
  const { data: tags } = useQuery(["tags"], () => getTags());

  const onChange = (filterKey: string, value: string[] | string) => {
    if (Array.isArray(value)) {
      const tagIds: string[] = [];
      for (const val of value) {
        tagIds.push(tags.find((tag: any) => tag.name === val).id);
      }
      setFilters((filters: any) => {
        return { ...filters, [filterKey]: tagIds };
      });
    } else {
      let tagId: string = "";
      tagId = tags.find((tag: any) => tag.name === value).id;
      setFilters((filters: any) => {
        return { ...filters, [filterKey]: tagId };
      });
    }
  };

  return (
    <>
      {games === null ? (
        <div className={styles.spinnerContainer}>
          <PacmanLoader color="#1b4559" size={30} />
        </div>
      ) : (
        <div className={styles.container}>
          <div className={styles.headerContainer}>
            <h1 className={styles.header}>Games</h1>
            <Search
              placeholder="Game name"
              enterButton
              className={styles.search}
              onSearch={setSearchText}
              style={{ width: "400px" }}
            />
          </div>
          <div className={styles.filter}>
            <MultipleSelect
              title="Player Types"
              filterKey="playerTypes"
              elements={tags
                ?.filter(
                  (tag: { tagType: string }) => tag.tagType === "PLAYER_TYPE"
                )
                .map((elem: { name: string }) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <MultipleSelect
              title="Genre"
              filterKey="genre"
              elements={tags
                ?.filter((tag: { tagType: string }) => tag.tagType === "GENRE")
                .map((elem: { name: string }) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <SingleSelect
              title="Production"
              filterKey="production"
              elements={tags
                ?.filter(
                  (tag: { tagType: string }) => tag.tagType === "PRODUCTION"
                )
                .map((elem: { name: string }) => elem.name)}
              reset={false}
              onChange={onChange}
            ></SingleSelect>
            <MultipleSelect
              title="Platform"
              filterKey="platform"
              elements={tags
                ?.filter(
                  (tag: { tagType: string }) => tag.tagType === "PLATFORM"
                )
                .map((elem: { name: string }) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <MultipleSelect
              title="Art Style"
              filterKey="artStyle"
              elements={tags
                ?.filter(
                  (tag: { tagType: string }) => tag.tagType === "ART_STYLE"
                )
                .map((elem: { name: string }) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>

            <Button
              onClick={() => setActiveFilters(filters)}
              className={styles.filterButton}
            >
              Filter
            </Button>
          </div>
          <div style={{ width: "100%", marginBottom: "10px" }}>
            <PromotedEntities games={promotedEntities} />
          </div>
          <div className={styles.games}>
            {Array.isArray(notPromotedgames) &&
              notPromotedgames.map((game) => {
                return <Game game={game} key={game.name}></Game>;
              })}
          </div>
        </div>
      )}
    </>
  );
}

export default Games;
