import { useState } from "react";
import { Button, Input, Rate } from "antd";
import styles from "./Review.module.scss";
import { CheckOutlined } from "@ant-design/icons";
import { useMutation, useQueryClient } from "react-query";
import { postReview } from "../../../Services/review";
const { TextArea } = Input;

function ReviewInput({ gameId }: { gameId: string }) {
  const [content, setContent] = useState("");
  const [rating, setRating] = useState(0);

  const queryClient = useQueryClient();

  const { mutate: addReview, isLoading } = useMutation(
    (review: any) => postReview(review),
    {
      onSuccess() {
        queryClient.invalidateQueries(["reviews", gameId, ""]);
      },
      onMutate(review: any) {
        queryClient.setQueryData(["reviews", gameId, ""], (prev: any) => [
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
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <Rate
          style={{ alignSelf: "flex-end" }}
          allowHalf
          defaultValue={0}
          onChange={setRating}
          value={rating}
        />
        <Button icon={<CheckOutlined />} onClick={() => handleClick()}></Button>
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
