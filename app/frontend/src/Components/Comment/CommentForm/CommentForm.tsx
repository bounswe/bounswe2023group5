import { useParams } from "react-router-dom";
import styles from "./CommentForm.module.scss";
import { Button, Form, Input } from "antd";
import { useMutation } from "react-query";
import { createComment } from "../../../Services/comment";

function CommentForm() {
  const postId = useParams();

  const { mutate: addComment, isLoading } = useMutation(
    ({  commentContent }: {  commentContent: string }) =>
      
      createComment({  post: postId.postId!, commentContent }),
    {
      onSuccess() {
        alert("Comment is added successfully!");
      },
    }
  );

  return (
    <div className={styles.container}>
      <Form onFinish={addComment}>
        <Form.Item
          name="commentContent"
          rules={[{ required: true, message: "Please enter a comment" }]}
        >
          <Input.TextArea
            rows={2}
            placeholder="Comment under construction... 🚧"
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" disabled={isLoading}>
            Submit
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default CommentForm;
