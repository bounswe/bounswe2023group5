import { StarFilled, StarOutlined } from "@ant-design/icons";
import styles from "./GameDetails.module.scss";
import { useState } from "react";
import Summary from "../../Components/GameDetails/Summary/Summary";
import { useParams } from "react-router-dom";
import { getGame } from "../../Services/gamedetail";
import { useQuery } from "react-query";

function formatDate(date: Date) {
  const day = date.getDate();
  const monthIndex = date.getMonth();
  const year = date.getFullYear();

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const monthName = monthNames[monthIndex];

  return `${day} ${monthName} ${year}`;
}

function GameDetails() {
  const { gameId } = useParams();

  const { data } = useQuery(["game", gameId], () => getGame(gameId!));

  const score = 4;
  const [subPage, setSubPage] = useState<"summary" | "reviews" | "forum">(
    "summary"
  );

  const date = new Date();
  return (
    <div className={styles.container}>
      <div className={styles.info}>
        <div className={styles.pictureContainer}>
          <img
            src={`${import.meta.env.VITE_APP_IMG_URL}${data?.gameIcon}`}
            alt={data.gameName}
          />
        </div>
        <div className={styles.titleContainer}>
          <div className={styles.name}>
            <h1>{data?.gameName}</h1>
            <span>{formatDate(date)}</span>
          </div>
          <div className={styles.starsContainer}>
            {[1, 1, 1, 1, 1].map((_, index) =>
              index <= score - 1 ? (
                <StarFilled key={index} />
              ) : (
                <StarOutlined key={index} />
              )
            )}
          </div>
        </div>
      </div>
      <div className={styles.menu}>
        {["summary", "reviews", "forum"].map((name) => (
          <button
            key={name}
            type="button"
            className={subPage === name ? styles.active : ""}
            onClick={() => setSubPage(name as any)}
            disabled
          >
            {name}
          </button>
        ))}
      </div>
      <div className={styles.subPage}>
        <Summary game={data} />
      </div>
    </div>
  );
}

export default GameDetails;
