import { useQuery } from "react-query";
import Review from "./Review";
import styles from "./Review.module.scss";
import ReviewInput from "./ReviewInput";
import { getAllReviews } from "../../../Services/review";
import { useState } from "react";
import { Input } from "antd";

function Reviews({ gameId }: { gameId: string }) {
  const [reviewedBy, setReviewedBy] = useState("");
  const [searchText, setSearchText] = useState("");

  const { Search } = Input;

  const { data: reviews, isLoading } = useQuery(
    ["reviews", gameId, reviewedBy],
    () =>
      reviewedBy === ""
        ? getAllReviews(gameId).then((res) => res.data)
        : getAllReviews(gameId, reviewedBy).then((res) => res.data)
  );

  return (
    <>
      <Search
        placeholder="Search by user or content"
        enterButton
        onSearch={setSearchText}
      />
      <div className={styles.reviewsSubpageContainer}>
        <ReviewInput gameId={gameId} />
        {reviews &&
          reviews
            ?.filter((review: any) => {
              return `${review.reviewDescription}${review.reviewedUser}`.includes(
                searchText
              );
            })
            .map((review: any) => <Review key={review.id} review={review} />)}
      </div>
    </>
  );
}
export default Reviews;
