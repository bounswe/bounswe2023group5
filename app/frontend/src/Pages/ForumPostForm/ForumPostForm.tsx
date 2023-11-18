import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./ForumPostForm.module.scss";
import { Button, Form, Input } from "antd";
import { useMutation } from "react-query";
import { createPost } from "../../Services/forum";

function ForumPostForm() {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const { mutate: addPost, isLoading } = useMutation(
    ({ title, postContent }: { title: string; postContent: string }) =>
      createPost({ forum: searchParams.get("forumId")!, title, postContent }),
    {
      onSuccess() {
        navigate(searchParams.get("redirect") ?? "/");
      },
    }
  );

  return (
    <div className={styles.container}>
      <Form onFinish={addPost}>
        <Form.Item
          name="title"
          rules={[{ required: true, message: "Please enter a title" }]}
        >
          <Input placeholder="Come up with a totes cool title my dude" />
        </Form.Item>
        <Form.Item
          name="postContent"
          rules={[{ required: true, message: "Please enter post content" }]}
        >
          <Input.TextArea
            rows={5}
            placeholder="Your posts gonna talk-the-talk, but can it walk-the-walk?"
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

export default ForumPostForm;
