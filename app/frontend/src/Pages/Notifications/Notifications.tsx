import { useQuery } from "react-query";
import { getNotifications } from "../../Services/notification";
import Notification from "../../Components/Notification/Notification";
import styles from "./Notifications.module.scss";

function Notifications() {
  const gameId: string = "841cbf45-90cc-47b7-a763-fa3a18218bf9";

  const { data: notifications, isLoading: isLoadingnotifications } = useQuery(
    ["notifications", gameId],
    () => getNotifications({ userId: gameId! })
  );

  return (
    <div className={styles.notificationPage}>
      <div className={styles.notificationContainer}>
        <div className={styles.notificationTitle}>Notifications</div>
        {!isLoadingnotifications &&
          notifications.map(
            (notification: any) =>
              !notification.isDeleted && (
                <Notification props={notification} key={notification.id} />
              )
          )}
      </div>
    </div>
  );
}

export default Notifications;
