import { useParams } from "react-router-dom";
import styles from "./CommentForm.module.scss";
import { Button, Form, Input } from "antd";
import { useMutation, useQueryClient } from "react-query";
import { edit } from "../../../Services/comment";

function CommentEditForm({commentId, commentContent}:{commentId:string, commentContent:string}) {
  const postId = useParams();
  const [form] = Form.useForm();
  const queryClient = useQueryClient();

  const { mutate: editComment, isLoading } = useMutation(
    ({ commentContent }: { commentContent: string }) =>
      edit({ id: commentId!, commentContent }),
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
  form.setFieldsValue({commentContent:commentContent})
  return (
    <div className={styles.container}>
      <Form form={form} onFinish={editComment}>
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

export default CommentEditForm;
