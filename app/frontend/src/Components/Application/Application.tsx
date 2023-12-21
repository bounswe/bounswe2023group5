import { TeamOutlined } from "@ant-design/icons";
import styles from "./Application.module.scss";
import { Button } from "antd";
import { useMutation, useQueryClient } from "react-query";
import { reviewApplication } from "../../Services/applications";
import { NotificationUtil } from "../../Library/utils/notification";
import { handleAxiosError } from "../../Library/utils/handleError";

function Application({ application }: { application: any }) {
  const queryClient = useQueryClient();

  const { mutate: approve } = useMutation(
    (applicationId: string) => reviewApplication(applicationId, "APPROVE"),
    {
      onSuccess() {
        queryClient.invalidateQueries(["applications"]);
        NotificationUtil.success("You successfully approved the application");
      },
      onError(error: any) {
        handleAxiosError(error);
      },
    }
  );

  const { mutate: reject } = useMutation(
    (applicationId: string) => reviewApplication(applicationId, "REJECT"),
    {
      onSuccess() {
        queryClient.invalidateQueries(["applications"]);
        NotificationUtil.success("You successfully rejected the application");
      },
      onError(error: any) {
        handleAxiosError(error);
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
            <div style={{ display: "flex", gap: "40px" }}></div>
            <div style={{ display: "flex", gap: "3px" }}>
              <Button onClick={async () => approve(application.id)}>
                Accept
              </Button>
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
