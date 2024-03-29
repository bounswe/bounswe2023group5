import styles from "./GameDetails.module.scss";
import { useEffect, useState } from "react";
import Summary from "../../Components/GameDetails/Summary/Summary";
import { useParams, useSearchParams } from "react-router-dom";
import { getGame } from "../../Services/gamedetail";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { PacmanLoader } from "react-spinners";
import Reviews from "../../Components/GameDetails/Review/Reviews";
import Forum from "../../Components/Forum/Forum";
import { formatDate } from "../../Library/utils/formatDate";
import { Button, Rate, Tooltip } from "antd";
import { useAuth } from "../../Components/Hooks/useAuth";
import { addGame, removeGame } from "../../Services/profile";
import { twj } from "tw-to-css";
import { Annotorious } from "@recogito/annotorious";
import { handleAxiosError } from "../../Library/utils/handleError";
import {
  createAnnotation,
  deleteAnnotation,
  updateAnnotation,
} from "../../Services/annotation";
import { NotificationUtil } from "../../Library/utils/notification";
import { InfoCircleOutlined } from "@ant-design/icons";

function GameDetails() {
  const { user, isLoggedIn, profile } = useAuth();
  const { gameId } = useParams();
  const queryClient = useQueryClient();

  const [isImageAnnotationsApplied, setIsImageAnnotationsApplied] =
    useState(false);

  const isFollowing = !!profile?.games?.find((game: any) => game.id === gameId);

  const pageUrl = window.location.href.replace("?back=/home", "");

  const isAdmin = user?.role === "ADMIN";

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
  const hideTagField = () => {
    const tagField = document.querySelector(".r6o-widget.r6o-tag");

    if (tagField) {
      tagField.style.display = "none";
    }
  };

  useEffect(() => {
    const imageElement = document.querySelector("#imageElement");

    if (imageElement) {
      imageElement.addEventListener("click", hideTagField);
    }
  }, [isLoading]);

  const score = data?.overallRating;
  const [searchParams] = useSearchParams();

  const [subPage, setSubPage] = useState<"summary" | "reviews" | "forum">(
    (searchParams.get("subPage") as any) ?? "summary"
  );

  const date = data?.releaseDate;

  const linkImageAnnotation = (elem: any) => {
    if (elem && isImageAnnotationsApplied === false) {
      const config = {
        image: elem,
        readOnly: !isAdmin,
      };

      const anno = new Annotorious(config);

      setIsImageAnnotationsApplied(true);

      anno
        .loadAnnotations(
          `${
            import.meta.env.VITE_APP_ANNOTATION_API_URL
          }/annotation/get-image-annotations?source=${pageUrl}`
        )
        .then(function (annotations) {})
        .catch((error) => {
          if (error instanceof SyntaxError) {
            return;
          }
          NotificationUtil.error("Error occurred while retrieving annotations");
        });

      anno.on("createAnnotation", async (annotation: any, _overrideId) => {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          const newId = pageUrl + "/" + annotation.id.replace("#", "");
          annotation.id = newId;
          await createAnnotation(annotation);
          NotificationUtil.success("You successfully create the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      anno.on("deleteAnnotation", async function (annotation: any) {
        try {
          const id = annotation.id;
          await deleteAnnotation(id);
          NotificationUtil.success("You successfully delete the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      anno.on("selectAnnotation", async function (annotation: any) {
        hideTagField();
      });

      anno.on("updateAnnotation", async function (annotation, _previous) {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          await updateAnnotation(annotation);
          NotificationUtil.success("You successfully update the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });
    }
  };
  return (
    <div className={styles.container}>
      <span>
        <Tooltip title="This page is annotable. If you are an admin you can add, edit, and delete annotations to image or description of the game.">
          <InfoCircleOutlined style={{ fontSize: "20px" }} />
        </Tooltip>
      </span>
      {isLoading ? (
        <div className={styles.errorContainer}>
          <PacmanLoader color="#1b4559" size={30} />
        </div>
      ) : (
        <>
          <div className={styles.info}>
            <div className={styles.pictureContainer} id="imageElement">
              <img
                ref={(elem) => linkImageAnnotation(elem)}
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
