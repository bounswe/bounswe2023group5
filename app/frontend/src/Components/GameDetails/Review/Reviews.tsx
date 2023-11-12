import { useQuery } from "react-query";
import Review from "./Review";
import styles from "./Review.module.scss";
import ReviewInput from "./ReviewInput";
import { getAllReviews } from "../../../Services/review";
import { useState } from "react";

function Reviews({ gameId }: { gameId: string }) {
  const [reviewedBy, setReviewedBy] = useState("");

  const { data: reviews, isLoading } = useQuery(
    ["reviews", gameId, reviewedBy],
    () =>
      reviewedBy === ""
        ? getAllReviews(gameId).then((res) => res.data)
        : getAllReviews(gameId, reviewedBy).then((res) => res.data)
  );

  return (
    <>
      <ReviewInput gameId={gameId} />
      {reviews && (
        <div className={styles.reviewsSubpageContainer}>
          {reviews?.map((review: any) => (
            <Review key={review.id} review={review} />
          ))}
        </div>
      )}
    </>
  );
}
export default Reviews;
