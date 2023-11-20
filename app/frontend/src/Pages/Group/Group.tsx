import { useParams } from "react-router-dom";
import styles from "./Group.module.scss";
import { useQuery } from "react-query";
import { getGroup } from "../../Services/group";
import { formatDate } from "../../Library/utils/formatDate";
import Forum from "../../Components/Forum/Forum";

function Group() {
  const { groupId } = useParams();

  const { data: group, isLoading } = useQuery(["group", groupId], () =>
    getGroup(groupId!)
  );

  return (
    <div className={styles.container}>
      <div className={styles.info}>
        <div className={styles.name}>
          <h1>{group.title}</h1>
          <span>{formatDate(group.createdAt)}</span>
        </div>
        <div className={styles.desc}>{group.description}</div>
      </div>
      <div className={styles.forumTitle}>Forum</div>
      <div className={styles.forum}>
        <Forum forumId={group.forum} />
      </div>
    </div>
  );
}

export default Group;
