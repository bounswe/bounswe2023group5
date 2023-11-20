import { useParams } from "react-router-dom";
import styles from "./CommentForm.module.scss";
import { Button, Form, Input } from "antd";
import { useMutation, useQueryClient } from "react-query";
import { createComment } from "../../../Services/comment";

function CommentForm() {
  const postId = useParams();
  const [form] = Form.useForm();
  const queryClient = useQueryClient();

  const { mutate: addComment, isLoading } = useMutation(
    ({ commentContent }: { commentContent: string }) =>
      createComment({ post: postId.postId!, commentContent }),
    {
      onSuccess() {
        queryClient.invalidateQueries(["comments", postId.postId]);
      },
      onMutate(comment: any) {
        queryClient.setQueryData(["comments", postId.postId], (prev: any) => {
          return prev?.filter((comments: any) => comment.id !== comments.id);
        });
      },
    }
  );

  return (
    <div className={styles.container}>
      <Form form={form} onFinish={addComment}>
        <Form.Item
          name="commentContent"
          rules={[{ required: true, message: "Please enter a comment" }]}
        >
          <Input.TextArea
            rows={2}

            placeholder="Comment under construction... 🚧"
          />
        </Form.Item>
        <Form.Item noStyle>
          <Button
            type="primary"
            htmlType="submit"
            disabled={isLoading}
            style={{ marginLeft: "85%" }}
          >
            Submit
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default CommentForm;
