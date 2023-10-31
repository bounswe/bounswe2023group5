import { Typography } from "antd";
import TagRenderer from "../../TagRenderer/TagRenderer";
import styles from "./Summary.module.scss";

function Summary({ game }: { game: any }) {
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
        <Typography>{game?.gameDescription}</Typography>
      </div>
      {game.minSystemReq && (
        <div className={styles.req}>
          <span>Min System Req: </span>
          {game.minSystemReq}
        </div>
      )}
    </div>
  );
}

export default Summary;
