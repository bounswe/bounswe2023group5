import { Typography } from "antd";
import TagRenderer from "../../TagRenderer/TagRenderer";
import styles from "./Summary.module.scss";
import { getAchievementByGame } from "../../../Services/achievement";
import Achievement from "../../Achievement/Achievement/Achievement";
import { useQuery } from "react-query";
import { handleAxiosError } from "../../../Library/utils/handleError.ts";
import { Recogito } from "@recogito/recogito-js";

import "@recogito/recogito-js/dist/recogito.min.css";
import {
  createAnnotation,
  deleteAnnotation,
  updateAnnotation,
} from "../../../Services/annotation.ts";
import { NotificationUtil } from "../../../Library/utils/notification.ts";
import { useEffect, useState } from "react";
import { useAuth } from "../../Hooks/useAuth.tsx";

function Summary({ game }: { game: any }) {
  const { user } = useAuth();
  const [isAnnotationsApplied, setIsAnnotationsApplied] = useState(false);
  const { data: achievements, isLoading: isLoadingAchievements } = useQuery(
    ["achievements", game.id],
    () => getAchievementByGame({ gameId: game.id! })
  );

  const pageUrl = window.location.href.replace("?back=/home", "");

  const isAdmin = user?.role === "ADMIN";

  const hideTagField = () => {
    const tagField = document.querySelector(".r6o-widget.r6o-tag");

    if (tagField) {
      tagField.style.display = "none";
    }
  };

  useEffect(() => {
    const textElement = document.querySelector("#textElement");

    if (textElement) {
      textElement.addEventListener("click", hideTagField);
    }
  }, [game]);

  const linkAnnotation = (elem: any) => {
    if (elem && isAnnotationsApplied === false) {
      const r = new Recogito({
        content: elem,
        readOnly: !isAdmin,
      });
      setIsAnnotationsApplied(true);

      r.loadAnnotations(
        `${
          import.meta.env.VITE_APP_ANNOTATION_API_URL
        }/annotation/get-source-annotations?source=${pageUrl}`
      )
        .then(function (annotations) {})
        .catch((error) => {
          if (error instanceof SyntaxError) {
            return;
          }
          NotificationUtil.error("Error occurred while retrieving annotations");
        });

      r.on("createAnnotation", async (annotation: any, overrideId) => {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          const newId = pageUrl + "/" + annotation.id.replace("#", "");
          annotation.id = newId;
          overrideId(newId);
          await createAnnotation(annotation);
          NotificationUtil.success("You successfully create the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      r.on("deleteAnnotation", async function (annotation: any) {
        try {
          const id = annotation.id;
          await deleteAnnotation(id);
          NotificationUtil.success("You successfully delete the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      r.on("selectAnnotation", async function (annotation: any) {
        hideTagField();
      });

      r.on("updateAnnotation", async function (annotation, _previous) {
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
    <div className={styles.summaryContainer}>
      <div className={styles.fieldContainer}>
        {game?.genre?.length > 0 && (
          <div className={styles.tagContainer}>
            Genre:
            <TagRenderer tags={game.genre} />
          </div>
        )}
        {game?.production && (
          <div className={styles.tagContainer}>
            Production:
            <TagRenderer tags={[game?.production]} />
          </div>
        )}
        {game?.playerTypes?.length > 0 && (
          <div className={styles.tagContainer}>
            Player Type:
            <TagRenderer tags={game?.playerTypes} />
          </div>
        )}
        {game?.duration && (
          <div className={styles.tagContainer}>
            Duration:
            <TagRenderer tags={[game?.duration]} />
          </div>
        )}
        {game?.artStyles?.length > 0 && (
          <div className={styles.tagContainer}>
            Art Styles:
            <TagRenderer tags={game?.artStyles} />
          </div>
        )}
        {game?.platforms?.length > 0 && (
          <div className={styles.tagContainer}>
            Platforms:
            <TagRenderer tags={game?.platforms} />
          </div>
        )}
        {game.developer && (
          <div className={styles.tagContainer}>
            Developer:
            <TagRenderer tags={[game?.developer]} />
          </div>
        )}
        {game?.otherTags?.length > 0 && (
          <div className={styles.tagContainer}>
            Other:
            <TagRenderer tags={game?.otherTags} />
          </div>
        )}
      </div>
      <div className={styles.summary}>
        <Typography ref={(elem) => linkAnnotation(elem)} id="textElement">
          {game?.gameDescription}
        </Typography>
      </div>
      {game.minSystemReq && (
        <div className={styles.req}>
          <span>Min System Req: </span>
          {game.minSystemReq}
        </div>
      )}

      {!isLoadingAchievements && achievements.length > 0 && (
        <div>
          <div className={styles.title}>Achievements</div>
          <div className={styles.row}>
            {achievements.map(
              (achievement: any) =>
                !achievement.isDeleted && (
                  <div className={styles.achievementContainer}>
                    <Achievement props={achievement} key={achievement.id} />
                  </div>
                )
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default Summary;
