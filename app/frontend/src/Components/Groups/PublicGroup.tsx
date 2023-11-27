import { TeamOutlined, UserOutlined } from "@ant-design/icons";
import styles from "./PublicGroup.module.scss";
import { Button, message } from "antd";
import TagRenderer from "../TagRenderer/TagRenderer";
import { formatDate } from "../../Library/utils/formatDate";
import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "react-query";
import { joinGroup, leaveGroup } from "../../Services/group";

function PublicGroup({ group }: { group: any }) {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { mutate: join } = useMutation(
    (groupId: string) => joinGroup(groupId),
    {
      onSuccess() {
        queryClient.invalidateQueries(["groups"]);
      },
      onError(error: any) {
        message.error(error.response.data);
      },
    }
  );

  const { mutate: leave } = useMutation(
    (groupId: string) => leaveGroup(groupId),
    {
      onSuccess() {
        queryClient.invalidateQueries(["groups"]);
      },
      onError(error: any) {
        message.error(error.response.data);
      },
    }
  );

  return (
    <div className={styles.group}>
      <div className={styles.header}>
        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <TeamOutlined />
          <div>
            <b>{group?.title}</b>
          </div>
          <TagRenderer tags={group?.tags} />
          <div
            style={{
              fontSize: "12px",
              alignSelf: "flex-end",
            }}
          ></div>
        </div>
      </div>
      <div className={styles.body}>
        <div className={styles.imgContainer}>
          <img src="../../../assets/images/guru.jpeg"></img>
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
                <Button type="dashed" onClick={() => leave(group.id)}>
                  Leave
                </Button>
              ) : (
                <Button onClick={() => join(group.id)}>Join</Button>
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

export default PublicGroup;
