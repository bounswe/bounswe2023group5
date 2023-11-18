import PrivateGroup from "../../Components/Groups/PrivateGroup";
import PublicGroup from "../../Components/Groups/PublicGroup";
import styles from "./Groups.module.scss";

function Groups() {
  return (
    <div className={styles.groupsContainer}>
      <PublicGroup></PublicGroup>
      <PrivateGroup></PrivateGroup>
    </div>
  );
}

export default Groups;
