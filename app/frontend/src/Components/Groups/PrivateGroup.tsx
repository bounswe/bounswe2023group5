import { TeamOutlined, UserOutlined } from "@ant-design/icons";
import styles from "./PrivateGroup.module.scss";
import { Button } from "antd";
import TagRenderer from "../TagRenderer/TagRenderer";
import { formatDate } from "../../Library/utils/formatDate";
import { useNavigate } from "react-router-dom";

function PrivateGroup({ group }: { group: any }) {
  const navigate = useNavigate();

  return (
    <div className={styles.group}>
      <div className={styles.header}>
        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <TeamOutlined />
          <div>
            <b>{group?.title}</b>
          </div>
          <TagRenderer tags={group?.tags} />
        </div>
      </div>
      <div className={styles.body}>
        <div className={styles.imgContainer}>
          <img src="../../../assets/images/group.png"></img>
        </div>
        <div className={styles.content}>
          <div className={styles.description}>
            <span>{group?.description}</span>
          </div>
          <div className={styles.footer}>
            <div style={{ display: "flex", gap: "40px" }}>
              <div>
                <UserOutlined></UserOutlined>{" "}
                {`${group?.members.length} members`}
              </div>
              <div style={{ fontSize: "13px" }}>
                <i>{`since ${formatDate(group.createdAt)}`}</i>
              </div>
            </div>
            <div style={{ display: "flex", gap: "3px" }}>
              {group.userJoined ? (
                <Button disabled>Joined</Button>
              ) : (
                <Button>Apply</Button>
              )}
              <Button onClick={() => navigate(`/group/detail/${group.id}`)}>
                Group Details
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PrivateGroup;
