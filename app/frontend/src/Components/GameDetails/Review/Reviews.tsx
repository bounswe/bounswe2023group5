import { useQuery } from "react-query";
import Review from "./Review";
import styles from "./ReviewSubPage.module.scss";
import ReviewInput from "./ReviewInput";
import { getAllReviews } from "../../../Services/review";
import { useState } from "react";
import { Button, Input, Select } from "antd";
import {
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
import { isEmpty } from "../../../Library/utils/isEmpty";

function Reviews({ gameId }: { gameId: string }) {
  const [reviewedBy, setReviewedBy] = useState();
  const [searchText, setSearchText] = useState("");

  const { Search } = Input;

  const sortOptions = [
    { label: "Creation Date", value: "CREATION_DATE" },
    { label: "Overall Vote", value: "OVERALL_VOTE" },
  ];

  const [sortBy, setSortBy] = useState<string>(sortOptions[0].value);
  const [sortDir, setSortDir] = useState<"ASCENDING" | "DESCENDING">(
    "DESCENDING"
  );
  const toggleSortDir = () => {
    setSortDir((currentSortDir) =>
      currentSortDir === "ASCENDING" ? "DESCENDING" : "ASCENDING"
    );
  };

  const { data: reviews, isLoading } = useQuery(
    ["reviews", gameId, reviewedBy, sortBy, sortDir],
    () =>
      getAllReviews(gameId, sortBy, sortDir, reviewedBy).then((res) => res.data)
  );

  return (
    <>
      <div className={styles.reviewsSubpageContainer}>
        <div className={styles.findReview}>
          <Search
            placeholder="Search reviews by content or reviewer"
            enterButton
            onSearch={setSearchText}
            style={{ minWidth: "600px", padding: "5px" }}
          />
          <Button onClick={toggleSortDir}>
            {sortDir === "DESCENDING" ? (
              <SortDescendingOutlined />
            ) : (
              <SortAscendingOutlined />
            )}
          </Button>
          <Select
            options={sortOptions}
            value={sortBy}
            onChange={setSortBy}
            style={{ width: "200px" }}
          />
        </div>
        <ReviewInput gameId={gameId} />
        {!isEmpty(reviews) ? (
          reviews
            ?.filter((review: any) => {
              return `${review.reviewDescription}${review.reviewedUser}`.includes(
                searchText
              );
            })
            .map((review: any) => <Review key={review.id} review={review} />)
        ) : (
          <span style={{ flexShrink: "0", width: "100%", textAlign: "center" }}>
            There are no reviews for this game yet
          </span>
        )}
      </div>
    </>
  );
}
export default Reviews;
