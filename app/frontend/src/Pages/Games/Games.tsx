import React, { useEffect, useState } from "react";
import { useMutation } from "react-query";
import styles from "./Games.module.scss";
import PacmanLoader from "react-spinners/PacmanLoader";
import Game from "../../Components/Game/Game";
import SingleSelect from "../../Components/SingleSelect/SingleSelect";
import MultipleSelect from "../../Components/MultipleSelect/MultipleSelect";
import { Button, Input } from "antd";

function Games() {
  const [games, setGames] = useState<any[] | null>(null);
  const [tags, setTags] = useState<any[] | null>(null);
  const [filters, setFilters] = useState<any>({
    playerType: [],
    genre: [],
    production: "",
    platform: [],
    artStyle: [],
  });

  const { Search } = Input;

  const getGames = async () => {
    return fetch("http://localhost:8080/api/game/get-game-list", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
  };
  const getTags = async () => {
    return fetch("http://localhost:8080/api/tag/get-all", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
  };

  const gamesMutation = useMutation(getGames, {
    onSuccess: async (data) => {
      setGames(await data.json());
    },
    onError: (error: any) => {
      console.log(error);
    },
  });

  const tagsMutation = useMutation(getTags, {
    onSuccess: async (data) => {
      setTags(await data.json());
    },
    onError: (error: any) => {
      console.log(error);
    },
  });

  useEffect(() => {
    setTimeout(() => {
      gamesMutation.mutate();
      tagsMutation.mutate();
      tags;
    }, 1000);
  }, []);

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
              allowClear
              className={styles.search}
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
