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

function Games() {
  const [filters, setFilters] = useState<any>({
    playerType: [],
    genre: [],
    production: "",
    platform: [],
    artStyle: [],
  });

  const { Search } = Input;
  const [searchText, setSearchText] = useState("");
  const [activeFilters, setActiveFilters] = useState();

  const { data: games } = useQuery(["games", activeFilters, searchText], () =>
    getGames(activeFilters, searchText.length <= 0 ? undefined : searchText)
  );

  const { data: tags } = useQuery(["tags"], getTags);

  const onChange = (filterKey: string, value: string) => {
    setFilters((filters: any) => {
      return { ...filters, [filterKey]: value };
    });
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
                .map((elem: { name: any }) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <MultipleSelect
              title="Genre"
              filterKey="genre"
              elements={tags
                ?.filter((tag: { tagType: string }) => tag.tagType === "GENRE")
                .map((elem: { name: any }) => elem.name)}
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
                .map((elem: { name: any }) => elem.name)}
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
                .map((elem: { name: any }) => elem.name)}
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
                .map((elem: { name: any }) => elem.name)}
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
          <div className={styles.games}>
            {Array.isArray(games) &&
              games.map((game) => {
                return <Game game={game} key={game.name}></Game>;
              })}
          </div>
        </div>
      )}
    </>
  );
}

export default Games;
