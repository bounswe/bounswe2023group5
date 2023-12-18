import { TeamOutlined, UserOutlined } from "@ant-design/icons";
import styles from "./Application.module.scss";
import { Button, message } from "antd";
import TagRenderer from "../TagRenderer/TagRenderer";
import { formatDate } from "../../Library/utils/formatDate";
import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "react-query";
import { joinGroup, leaveGroup } from "../../Services/group";
import { reviewApplication } from "../../Services/applications";
import { NotificationUtil } from "../../Library/utils/notification";


function Application({ application }: { application: any }) {
  const navigate = useNavigate();
  const queryClient = useQueryClient();



  const { mutate: approve  } = useMutation(
    (applicationId: string) => reviewApplication(applicationId, "APPROVE"),
    {
      onSuccess() {
        queryClient.invalidateQueries(["applications"]);
        NotificationUtil.success("You successfully approved the application");
      },
      onError(error: any) {
        message.error(error.response.data);
      },
    }
  );

  const { mutate: reject  } = useMutation(
    (applicationId: string) => reviewApplication(applicationId, "REJECT"),
    {
      onSuccess() {
        queryClient.invalidateQueries(["applications"]);
        NotificationUtil.success("You successfully rejected the application");

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
            <b>{application?.applicant.username}</b>
          </div>
          <div
            style={{
              fontSize: "12px",
              alignSelf: "flex-end",
            }}
          ></div>
        </div>
      </div>
      <div className={styles.body}>
        
        <div className={styles.content}>
          <div className={styles.description}>
            <span>{application?.message}</span>
          </div>
          <div className={styles.footer}>
            <div style={{ display: "flex", gap: "40px" }}>
            
            </div>
            <div style={{ display: "flex", gap: "3px" }}>
              
              <Button onClick={async () => approve(application.id)}>Accept</Button>
              <Button onClick={async () => reject(application.id)}>
                Reject
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Application;
