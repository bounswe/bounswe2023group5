import { Button, Rate } from "antd";
import styles from "./Review.module.scss";
import {
  CheckOutlined,
  DeleteOutlined,
  DownOutlined,
  EditOutlined,
  StarFilled,
  UpOutlined,
} from "@ant-design/icons";
import TextArea from "antd/es/input/TextArea";
import { useState } from "react";
import { formatDate } from "../../../Library/utils/formatDate";
import { useAuth } from "../../Hooks/useAuth";
import { useMutation, useQueryClient } from "react-query";
import { deleteReview, updateReview } from "../../../Services/review";
import { useVote } from "../../Hooks/useVote";

function Review({ review }: { review: any }) {
  const [inputMode, setInputMode] = useState(false);
  const [reviewContent, setReviewContent] = useState(review.reviewDescription);
  const [rating, setRating] = useState(review.rating);

  const { user } = useAuth();
  const queryClient = useQueryClient();

  const { upvote, downvote } = useVote({
    voteType: "REVIEW",
    typeId: review.id,
    invalidateKey: ["reviews", review.gameId, ""],
  });

  const { mutate: removeReview } = useMutation(
    (id: string) => deleteReview(id),
    {
      onSuccess() {
        queryClient.invalidateQueries(["reviews", review.gameId]);
      },
      onMutate(id: string) {
        queryClient.setQueriesData(["reviews", review.gameId], (prev: any) => {
          return prev?.filter((review: any) => id !== review.id);
        });
      },
    }
  );

  const { mutate: editReview } = useMutation(
    ({ id, updatedReview }: { id: string; updatedReview: any }) =>
      updateReview(id, updatedReview),
    {
      onSuccess() {
        queryClient.invalidateQueries(["reviews", review.gameId]);
        setInputMode(false);
      },
      onMutate({ id, updatedReview }) {
        queryClient.setQueriesData(["reviews", review.gameId], (prev: any) =>
          prev?.map((review: any) =>
            review.id === id ? { ...review, ...updatedReview } : review
          )
        );
      },
    }
  );

  return (
    <div className={styles.reviewContainer} id={review.id}>
      <div className={styles.vote}>
        <Button
          type="primary"
          shape="circle"
          icon={<UpOutlined />}
          onClick={upvote}
        />
        <span>{review.overallVote}</span>
        <Button
          type="primary"
          shape="circle"
          icon={<DownOutlined />}
          onClick={downvote}
        />
      </div>
      <div className={styles.review}>
        <div className={styles.header}>
          <div className={styles.user}>
            <b>{review.reviewedUser}</b>
          </div>
          {user.username === review.reviewedUser && (
            <div className={styles.buttons}>
              {!inputMode ? (
                <Button
                  type="text"
                  ghost={true}
                  shape="circle"
                  size="small"
                  icon={<EditOutlined style={{ color: "#aacdbe" }} />}
                  onClick={() => setInputMode(true)}
                />
              ) : (
                <Button
                  type="text"
                  ghost={true}
                  shape="circle"
                  size="small"
                  icon={<CheckOutlined style={{ color: "#aacdbe" }} />}
                  onClick={() =>
                    editReview({
                      id: review.id,
                      updatedReview: {
                        rating: rating,
                        reviewDescription: reviewContent,
                      },
                    })
                  }
                />
              )}
              <Button
                type="text"
                ghost={true}
                shape="circle"
                size="small"
                icon={<DeleteOutlined style={{ color: "#aacdbe" }} />}
                onClick={() => removeReview(review.id)}
              />
            </div>
          )}
        </div>
        <div className={styles.date}>{formatDate(review.createdAt)}</div>
        <div className={styles.stars}>
          {inputMode ? (
            <Rate
              allowHalf
              defaultValue={rating}
              onChange={setRating}
              value={rating}
            />
          ) : (
            <>
              {[1, 2, 3, 4, 5].map((starValue) =>
                starValue <= review.rating ? (
                  <StarFilled key={starValue} />
                ) : null
              )}
              {Math.round(review.rating) !== review.rating && <span>½</span>}
            </>
          )}
        </div>
        {inputMode ? (
          <TextArea
            style={{ backgroundColor: "#022b40", color: "#faf7cf" }}
            defaultValue={reviewContent}
            onChange={(e) => setReviewContent(e.target.value)}
          />
        ) : (
          <div className={styles.content}>{review.reviewDescription}</div>
        )}
      </div>
    </div>
  );
}

export default Review;
