import styles from "./GameDetails.module.scss";
import { useState } from "react";
import Summary from "../../Components/GameDetails/Summary/Summary";
import { useParams, useSearchParams } from "react-router-dom";
import { getGame } from "../../Services/gamedetail";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { PacmanLoader } from "react-spinners";
import Reviews from "../../Components/GameDetails/Review/Reviews";
import Forum from "../../Components/Forum/Forum";
import { formatDate } from "../../Library/utils/formatDate";
import { Button, Rate } from "antd";
import { useAuth } from "../../Components/Hooks/useAuth";
import { addGame, removeGame } from "../../Services/profile";
import { twj } from "tw-to-css";

function GameDetails() {
  const { user, isLoggedIn, profile } = useAuth();
  const { gameId } = useParams();
  const queryClient = useQueryClient();

  const isFollowing = !!profile?.games?.find((game: any) => game.id === gameId);

  const { mutate: follow } = useMutation(
    () => addGame({ profileId: profile.id, gameId: gameId! }),
    {
      onSuccess() {
        queryClient.invalidateQueries(["profile"]);
      },
    }
  );
  const { mutate: unfollow } = useMutation(
    () => removeGame({ profileId: profile.id, gameId: gameId! }),
    {
      onSuccess() {
        queryClient.invalidateQueries(["profile"]);
      },
    }
  );

  const { data, isLoading } = useQuery(["game", gameId], () =>
    getGame(gameId!)
  );

  const score = data?.overallRating;
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
              {isLoggedIn &&
                (isFollowing ? (
                  <Button
                    type="primary"
                    className={styles.followButton}
                    onClick={() => unfollow()}
                    style={twj("opacity-50")}
                  >
                    Unfollow
                  </Button>
                ) : (
                  <Button
                    type="primary"
                    className={styles.followButton}
                    onClick={() => follow()}
                  >
                    Follow
                  </Button>
                ))}

              <div className={styles.name}>
                <h1>{data?.gameName}</h1>
                <span>{formatDate(date)}</span>
              </div>
              <div className={styles.starsContainer}>
                {score ? (
                  <Rate
                    allowHalf
                    style={{ fontSize: "50px" }}
                    disabled
                    defaultValue={Math.round(score * 2) / 2}
                  />
                ) : (
                  "No rate is given for this game yet. Be the first one!"
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
                    redirect={`/game/detail/${gameId}?subPage=forum`}
                    gameId={gameId}
                  />
                ) : (
                  <>No forum on this game.</>
                )
              ) : (
                <Reviews gameId={data.id} />
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default GameDetails;
