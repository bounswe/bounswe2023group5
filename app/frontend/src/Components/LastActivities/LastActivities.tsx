import { getActivities } from "../../Services/activities";
import { useQuery } from "react-query";
import styles from "./LastActivities.module.scss";
import { CommentOutlined } from "@ant-design/icons";
import { Card } from "antd";

function LastActivities() {
  const activities = useQuery(["activites"], () => getActivities());
  console.log(activities.data);
  return (
    <div className={styles.container}>
      {!activities.data ? (
        <p>No activities</p>
      ) : (
        activities.data.map((activity: any) => {
          return (
            <>
              {activity.type === "COMMENT" ? (
                <Card
                  type="inner"
                  title="You Commented"
                  extra={<CommentOutlined />}
                  size="small"
                >
                  {`"${activity.description}"`}
                </Card>
              ) : activity.type === "REVIEW" ? (
                <Card
                  type="inner"
                  title="You Reviewed"
                  extra={<CommentOutlined />}
                  size="small"
                >
                  {`"${activity.description}"`}
                </Card>
              ) : (
                activity.type === "POST" && (
                  <Card
                    type="inner"
                    title="You Posted"
                    extra={<CommentOutlined />}
                    size="small"
                  >
                    {`"${activity.description}"`}
                  </Card>
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
