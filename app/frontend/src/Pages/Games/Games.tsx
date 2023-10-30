import React, { useEffect, useState } from "react";
import { useMutation, useQuery } from "react-query";
import styles from "./Games.module.scss";
import PacmanLoader from "react-spinners/PacmanLoader";
import Game from "../../Components/Game/Game";
import SingleSelect from "../../Components/SingleSelect/SingleSelect";
import MultipleSelect from "../../Components/MultipleSelect/MultipleSelect";
import { Button, Input } from "antd";

function Games() {
  const [filters, setFilters] = useState<any>({
    playerType: [],
    genre: [],
    production: "",
    platform: [],
    artStyle: [],
  });

  const { Search } = Input;

  const { data: games, status } = useQuery(["games", filters], () => getGames);

  //const { data, status } = useQuery("tags", getTags);

  const onChange = (filterKey: string, value: string) => {
    setFilters((filters) => {
      return { ...filters, [filterKey]: value };
    });
  };

  const onFilter = () => {
    fetch("http://localhost:8080/api/game/get-game-list", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(filters),
    })
      .then(async (res) => {
        setGames(await res.json());
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const handleSearch = (search: string) => {
    getGames(null, search);
    fetch("http://localhost:8080/api/game/get-game-list", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ search }),
    })
      .then(async (res) => {
        setGames(await res.json());
      })
      .catch((error) => {
        console.log(error);
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
              onSearch={(elem) => handleSearch(elem)}
            />
          </div>
          <div className={styles.filter}>
            <MultipleSelect
              title="Player Types"
              filterKey="playerTypes"
              elements={tags
                ?.filter((tag) => tag.tagType === "PLAYER_TYPE")
                .map((elem) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <MultipleSelect
              title="Genre"
              filterKey="genre"
              elements={tags
                ?.filter((tag) => tag.tagType === "GENRE")
                .map((elem) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <SingleSelect
              title="Production"
              filterKey="production"
              elements={tags
                ?.filter((tag) => tag.tagType === "PRODUCTION")
                .map((elem) => elem.name)}
              reset={false}
              onChange={onChange}
            ></SingleSelect>
            <MultipleSelect
              title="Platform"
              filterKey="platform"
              elements={tags
                ?.filter((tag) => tag.tagType === "PLATFORM")
                .map((elem) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>
            <MultipleSelect
              title="Art Style"
              filterKey="artStyle"
              elements={tags
                ?.filter((tag) => tag.tagType === "ART_STYLE")
                .map((elem) => elem.name)}
              reset={false}
              onChange={onChange}
            ></MultipleSelect>

            <Button onClick={onFilter} className={styles.filterButton}>
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
