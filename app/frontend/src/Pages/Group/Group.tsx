import { useParams } from "react-router-dom";
import styles from "./Group.module.scss";
import { useQuery } from "react-query";
import { getGroup } from "../../Services/group";
import { formatDate } from "../../Library/utils/formatDate";
import Forum from "../../Components/Forum/Forum";
import { getGame } from "../../Services/gamedetail";
import Game from "../../Components/Game/Game";
import TagRenderer from "../../Components/TagRenderer/TagRenderer";

function Group() {
  const { groupId } = useParams();

  const { data: group, isLoading } = useQuery(["group", groupId], () =>
    getGroup(groupId!)
  );

  const { data: game } = useQuery(
    ["game", group?.gameId],
    () => getGame(group?.gameId!),
    { enabled: !!group }
  );

  return (
    <div className={styles.container}>
      <div className={styles.info}>
        <div className={styles.game}>{game && <Game game={game} />}</div>
        <div className={styles.meta}>
          <div className={styles.name}>
            <h1>{group?.title}</h1>
            <span>{formatDate(group?.createdAt)}</span>
            <span>{!isLoading && <TagRenderer tags={group?.tags} />}</span>
          </div>
          <div className={styles.desc}>{group?.description}</div>
        </div>
      </div>
      <div className={styles.forumTitle}>Forum</div>
      <div className={styles.forum}>
        {!isLoading && (
          <Forum
            forumId={group?.forumId}
            redirect={`/group/detail/${groupId}`}
          />
        )}
      </div>
    </div>
  );
}

export default Group;
