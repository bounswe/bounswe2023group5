import { Button } from "antd";
import styles from "./Review.module.scss";
import {
  DeleteOutlined,
  DownOutlined,
  EditOutlined,
  StarFilled,
  UpOutlined,
} from "@ant-design/icons";
import TextArea from "antd/es/input/TextArea";
import { useState } from "react";
import { formatDate } from "../../../Library/utils/formatDate";

function Review({ review }: { review: any }) {
  const [inputMode, setInputMode] = useState(false);

  return (
    <div className={styles.reviewContainer} id={review.id}>
      <div className={styles.vote}>
        <Button type="primary" shape="circle" icon={<UpOutlined />} />
        <span>{review.overallVote}</span>
        <Button type="primary" shape="circle" icon={<DownOutlined />} />
      </div>
      <div className={styles.review}>
        <div className={styles.header}>
          <div className={styles.user}>
            <b>{review.reviewedBy}</b>
          </div>
          <div className={styles.buttons}>
            <Button
              type="text"
              ghost={true}
              shape="circle"
              size="small"
              icon={<EditOutlined style={{ color: "#aacdbe" }} />}
              onClick={() => setInputMode(true)}
            />
            <Button
              type="text"
              ghost={true}
              shape="circle"
              size="small"
              icon={<DeleteOutlined style={{ color: "#aacdbe" }} />}
            />
          </div>
        </div>
        <div className={styles.date}>{formatDate(review.createdAt)}</div>
        <div className={styles.stars}>
          {[1, 2, 3, 4, 5].map((starValue) =>
            starValue <= review.rating ? <StarFilled /> : ""
          )}
          {Math.round(review.rating) !== review.rating ? <span>½</span> : ""}
        </div>
        {inputMode ? (
          <TextArea />
        ) : (
          <div className={styles.content}>{review.reviewDescription}</div>
        )}
      </div>
    </div>
  );
}

export default Review;
