import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./ForumPostForm.module.scss";
import { Button, Form, Input, Select } from "antd";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { createPost, editPost, getPost } from "../../Services/forum";
import { twj } from "tw-to-css";
import { useEffect } from "react";
import { useForm } from "antd/es/form/Form";
import { getTags } from "../../Services/tags";

function ForumPostForm() {
  const [form] = useForm();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const editId = searchParams.get("editId");
  const queryClient = useQueryClient();

  const { data: editedPost, isLoading: editLoading } = useQuery(
    ["post", editId],
    () => getPost(editId!),
    {
      enabled: !!editId,
    }
  );

  const { data: tagOptions } = useQuery(
    ["tagOptions", "forumPost"],
    async () => {
      const data = await getTags({ tagType: "POST" });
      return data.map((item: { name: any; id: any }) => ({
        label: item.name,
        value: item.id,
      }));
    }
  );

  useEffect(() => {
    if (editedPost) {
      form.setFieldsValue(editedPost);
    }
  }, [editedPost]);

  const { mutate: submit, isLoading } = useMutation(
    ({
      title,
      postContent,
      tags,
    }: {
      title: string;
      postContent: string;
      tags: string[];
    }) => {
      if (!editId) {
        return createPost({
          forum: searchParams.get("forumId")!,
          title,
          postContent,
          tags,
        });
      } else {
        return editPost({ id: editId!, title, postContent });
      }
    },
    {
      onSuccess() {
        queryClient.invalidateQueries(["post", editId]);
        queryClient.invalidateQueries(["forum", searchParams.get("forumId")]);

        navigate(searchParams.get("redirect") ?? "/");
      },
    }
  );

  return (
    <div className={styles.container}>
      <Form onFinish={submit} layout="vertical" form={form}>
        <Form.Item
          name="title"
          rules={[{ required: true, message: "Please enter a title" }]}
          label="Title"
        >
          <Input placeholder="Come up with a totes cool title my dude" />
        </Form.Item>
        <Form.Item name="tags" label="Tags">
          <Select
            allowClear
            mode="multiple"
            placeholder="You can add tags to your post."
            options={tagOptions}
          />
        </Form.Item>
        <Form.Item
          name="postContent"
          rules={[{ required: true, message: "Please enter post content" }]}
          label="Content"
        >
          <Input.TextArea
            rows={5}
            placeholder="Your posts gonna talk-the-talk, but can it walk-the-walk?"
          />
        </Form.Item>
        <Form.Item noStyle>
          <Button
            type="primary"
            htmlType="submit"
            disabled={isLoading || editLoading}
            style={twj("ml-[85%]")}
          >
            Submit
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default ForumPostForm;
