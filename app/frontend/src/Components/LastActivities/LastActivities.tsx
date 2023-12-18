import { getActivities } from "../../Services/activities";
import { useQuery } from "react-query";
import styles from "./LastActivities.module.scss";
import {
  CommentOutlined,
  DownCircleOutlined,
  TeamOutlined,
  UpCircleOutlined,
} from "@ant-design/icons";
import { Card } from "antd";
import { Link } from "react-router-dom";

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
            <div className={styles.subItem}>
              {(() => {
                if (activity.type === "COMMENT") {
                  return (
                    <Card
                      type="inner"
                      title="You Commented"
                      extra={<CommentOutlined />}
                      size="small"
                    >
                      {`"${activity.description}"`}
                    </Card>
                  );
                } else if (activity.type === "REVIEW") {
                  return (
                    <Card
                      type="inner"
                      title="You Reviewed"
                      extra={
                        <Link
                          to={`../${activity.parentType}/detail/${activity.parentId}`}
                        >
                          Details
                        </Link>
                      }
                      size="small"
                    >
                      {`"${activity.description}"`}
                    </Card>
                  );
                } else if (activity.type === "POST") {
                  return (
                    <Card
                      type="inner"
                      title="You Posted"
                      extra={
                        <Link
                          to={`../forum/detail/${activity.parentId}/${activity.typeId}`}
                        >
                          Details
                        </Link>
                      }
                      size="small"
                    >
                      {`"${activity.description}"`}
                    </Card>
                  );
                } else if (activity.type === "VOTE") {
                  return (
                    <Card
                      type="inner"
                      title="You Voted"
                      extra={
                        activity.description === "UPVOTE" ? (
                          <UpCircleOutlined />
                        ) : (
                          <DownCircleOutlined />
                        )
                      }
                      size="small"
                    >
                      {`"${activity.description}"`}
                    </Card>
                  );
                } else if (activity.type === "GROUP") {
                  return (
                    <Card
                      type="inner"
                      title="You Joined A Group"
                      extra={<TeamOutlined></TeamOutlined>}
                      size="small"
                    >
                      {`"${activity.description}"`}
                    </Card>
                  );
                }
                // Add more else-if conditions as needed
                return null; // Optional: Default case or no-op
              })()}
            </div>
          );
        })
      )}
    </div>
  );
}

export default LastActivities;
//
