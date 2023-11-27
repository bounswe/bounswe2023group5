import { useState } from "react";
import { Button, Input, Rate } from "antd";
import styles from "./ReviewInput.module.scss";
import { CheckOutlined } from "@ant-design/icons";
import { useMutation, useQueryClient } from "react-query";
import { postReview } from "../../../Services/review";
import { useAuth } from "../../Hooks/useAuth";
const { TextArea } = Input;

function ReviewInput({ gameId }: { gameId: string }) {
  const { refreshProfile } = useAuth();
  const [content, setContent] = useState("");
  const [rating, setRating] = useState(0);

  const queryClient = useQueryClient();

  const { mutate: addReview, isLoading } = useMutation(
    (review: any) => postReview(review),
    {
      onSuccess() {
        queryClient.invalidateQueries(["reviews", gameId]);
        refreshProfile();
      },
      onMutate(review: any) {
        queryClient.setQueriesData(["reviews", gameId], (prev: any) => [
          review,
          ...prev,
        ]);
      },
    }
  );

  const handleClick = () => {
    const review = {
      reviewDescription: content,
      rating: rating,
      gameId: gameId,
    };
    addReview(review);
  };

  return (
    <div className={styles.reviewInputContainer}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <div className={styles.header}>
          <div>
            <b>Post Review</b>
          </div>
          <Rate
            style={{ alignSelf: "flex-end" }}
            allowHalf
            defaultValue={0}
            onChange={setRating}
            value={rating}
          />
        </div>
        <Button
          icon={<CheckOutlined />}
          type="text"
          onClick={() => handleClick()}
        ></Button>
      </div>

      <TextArea
        style={{ backgroundColor: "#022b40", color: "#faf7cf" }}
        value={content}
        onChange={(e) => setContent(e.target.value)}
        autoSize={{ minRows: 3, maxRows: 5 }}
      />
    </div>
  );
}

export default ReviewInput;
