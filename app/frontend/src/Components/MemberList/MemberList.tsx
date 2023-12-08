import { Button, List, Select } from "antd";
import styles from "./MemberList.module.scss";
import { useMutation } from "react-query";
import { banUserFromGroup } from "../../Services/group";
import { NotificationUtil } from "../../Library/utils/notification";
import { handleError } from "../../Library/utils/handleError";
interface MemberListData {
  id: string;
  username: string;
  photoUrl: string;
}

function MemberList({
  data,
  groupId,
}: {
  data: MemberListData[];
  groupId: string;
}) {
  const banUserMutation = useMutation(banUserFromGroup, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully banned the user");
    },
    onError: (error) => {
      handleError(error);
    },
  });

  const handleClick = (userId: string) => {
    banUserMutation.mutate({ groupId, userId });
  };
  return (
    <List
      dataSource={data}
      renderItem={(item: MemberListData) => (
        <List.Item key={item.id}>
          <List.Item.Meta
            //avatar={<Avatar src={item.photoUrl} />}
            title={<p className={styles.username}>{item.username}</p>}
          />
          <Button type="primary" danger onClick={() => handleClick(item.id)}>
            Ban
          </Button>
        </List.Item>
      )}
    />
  );
}

export default MemberList;
