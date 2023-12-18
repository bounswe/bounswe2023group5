import { getActivities } from "../../Services/activities";
import { useQuery } from "react-query";
import styles from "./LastActivities.module.scss";

function LastActivities() {
  const activities = useQuery(["activites"], () => getActivities());

  return (
    <div className={styles.container}>
      {!activities.data ? (
        <p>No activities</p>
      ) : (
        activities.data.map((activity) => {
          return (
            <>
              {activity.type === "COMMENT" ? (
                <div className={styles.activity}>
                  <p className={styles.content}>
                    You commented {activity.description}
                  </p>
                </div>
              ) : activity.type === "REVIEW" ? (
                <div className={styles.activity}>
                  <p className={styles.content}>
                    You reviewed {activity.description}
                  </p>
                </div>
              ) : (
                activity.type === "POST" && (
                  <div className={styles.activity}>
                    <p className={styles.content}>
                      You posted {activity.description}
                    </p>
                  </div>
                )
              )}
            </>
          );
        })
      )}
    </div>
  );
}

export default LastActivities;
//
