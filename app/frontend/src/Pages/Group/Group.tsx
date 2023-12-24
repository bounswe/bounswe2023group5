import { useNavigate, useNavigation, useParams } from "react-router-dom";
import styles from "./Group.module.scss";
import { useMutation, useQuery, useQueryClient } from "react-query";
import {
  applyGroup,
  deleteGroup,
  getGroup,
  joinGroup,
  leaveGroup,
} from "../../Services/group";
import { formatDate } from "../../Library/utils/formatDate";
import Forum from "../../Components/Forum/Forum";
import { getGame } from "../../Services/gamedetail";
import Game from "../../Components/Game/Game";
import TagRenderer from "../../Components/TagRenderer/TagRenderer";
import { Button, Modal, message } from "antd";
import { useState } from "react";
import MemberList from "../../Components/MemberList/MemberList";
import { useAuth } from "../../Components/Hooks/useAuth";
import { NotificationUtil } from "../../Library/utils/notification";
import { handleError } from "../../Library/utils/handleError";

function Group() {
  const { groupId } = useParams();
  const { user } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { data: group, isLoading } = useQuery(["group", groupId], () =>
    getGroup(groupId!)
  );

  const { data: game } = useQuery(
    ["game", group?.gameId],
    () => getGame(group?.gameId!),
    { enabled: !!group }
  );

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const deleteGroupMutation = useMutation(deleteGroup, {
    onSuccess: async () => {
      NotificationUtil.success(`You successfully delete the group`);
      navigate("/groups");
    },
    onError: (error) => {
      handleError(error);
    },
  });

  const handleClick = () => {
    deleteGroupMutation.mutate(groupId as string);
  };

  const handleReview = () => {
    navigate(`/group/review-application/${groupId}`);
  };

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

  const apply = async () => {
    try {
      const response = await applyGroup(group.id);
      if (response) {
        NotificationUtil.success("You successfully applied to the group");
      }
    } catch (error: any) {
      NotificationUtil.error(error.response.data);
      console.log(error);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.info}>
        <div className={styles.groupImage}>
          <img
            src={
              group?.groupIcon
                ? `${import.meta.env.VITE_APP_IMG_URL}${group.groupIcon}`
                : "../../../assets/images/group.png"
            }
          ></img>
        </div>
        <div className={styles.meta}>
          <div className={styles.name}>
            <h1>{group?.title}</h1>
            <span>{formatDate(group?.createdAt)}</span>
            <span>{!isLoading && <TagRenderer tags={group?.tags} />}</span>
          </div>
          <div className={styles.desc}>{group?.description}</div>
          <div className={styles.moderation}>
            {group?.userJoined ? (
              <Button type="primary" onClick={() => leave(groupId!)}>
                Leave
              </Button>
            ) : group?.membershipPolicy === "PUBLIC" ? (
              <Button type="primary" onClick={() => join(groupId!)}>
                Join
              </Button>
            ) : (
              <Button type="primary" onClick={apply}>
                Apply
              </Button>
            )}
            <Button type="primary" onClick={showModal}>
              {`Members (${group?.members.length || 0})`}
            </Button>
            {group?.moderators.some(
              (moderator: any) => moderator.id === user?.id
            ) &&
              group.membershipPolicy === "PRIVATE" && (
                <div>
                  <Button type="primary" onClick={handleReview}>
                    Review Applications
                  </Button>
                </div>
              )}
            {(user?.role === "ADMIN" ||
              group?.moderators.includes(user?.id)) && (
              <div>
                <Button type="primary" onClick={handleClick} danger>
                  Delete Group
                </Button>
              </div>
            )}
          </div>
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

      <Modal
        title="Members"
        open={isModalOpen}
        footer={null}
        onCancel={handleCancel}
      >
        <MemberList
          data={group?.members}
          groupId={groupId as string}
        ></MemberList>
      </Modal>
    </div>
  );
}

export default Group;
