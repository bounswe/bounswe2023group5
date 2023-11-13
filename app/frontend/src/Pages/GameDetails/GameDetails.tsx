import { StarFilled, StarOutlined } from "@ant-design/icons";
import styles from "./GameDetails.module.scss";
import { useState } from "react";
import Summary from "../../Components/GameDetails/Summary/Summary";
import { useParams, useSearchParams } from "react-router-dom";
import { getGame } from "../../Services/gamedetail";
import { useQuery } from "react-query";
import { PacmanLoader } from "react-spinners";
import Forum from "../../Components/Forum/Forum";
import { formatDate } from "../../Library/utils/formatDate";

function GameDetails() {
  const { gameId } = useParams();

  const { data, isLoading } = useQuery(["game", gameId], () =>
    getGame(gameId!)
  );

  const score = 4;
  const [searchParams] = useSearchParams();

  const [subPage, setSubPage] = useState<"summary" | "reviews" | "forum">(
    (searchParams.get("subPage") as any) ?? "summary"
  );

  const date = data?.releaseDate;
  return (
    <div className={styles.container}>
      {isLoading ? (
        <div className={styles.errorContainer}>
          <PacmanLoader color="#1b4559" size={30} />
        </div>
      ) : (
        <>
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
              >
                {name}
              </button>
            ))}
          </div>
          {data && (
            <div className={styles.subPage}>
              {subPage === "summary" ? (
                <Summary game={data} />
              ) : subPage === "forum" ? (
                data?.forum ? (
                  <Forum
                    forumId={data.forum}
                    redirect={`/game/${gameId}?subPage=forum`}
                  />
                ) : (
                  <>No forum on this game.</>
                )
              ) : (
                <></>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default GameDetails;
