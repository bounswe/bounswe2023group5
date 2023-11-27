import { useNavigate, useNavigation, useParams } from "react-router-dom";
import styles from "./Group.module.scss";
import { useMutation, useQuery } from "react-query";
import { deleteGroup, getGroup } from "../../Services/group";
import { formatDate } from "../../Library/utils/formatDate";
import Forum from "../../Components/Forum/Forum";
import { getGame } from "../../Services/gamedetail";
import Game from "../../Components/Game/Game";
import TagRenderer from "../../Components/TagRenderer/TagRenderer";
import { Button, Modal } from "antd";
import { useState } from "react";
import MemberList from "../../Components/MemberList/MemberList";
import { useAuth } from "../../Components/Hooks/useAuth";

function Group() {
  const { groupId } = useParams();
  const { user } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();

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
      alert(`You successfully delete the group`);
      navigate("/groups");
    },
    onError: () => {
      alert("Something went wrong");
    },
  });

  const handleClick = () => {
    deleteGroupMutation.mutate(groupId as string);
  };

  console.log(user);
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
          <div className={styles.moderation}>
            <Button type="primary" onClick={showModal}>
              {`Members (${group?.members.length || 0})`}
            </Button>
            {(user?.role === "ADMIN" ||
              group?.moderators.includes(user.id)) && (
              <Button type="primary" onClick={handleClick} danger>
                Delete Group
              </Button>
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
