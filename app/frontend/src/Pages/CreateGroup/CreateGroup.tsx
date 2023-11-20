import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./CreateGroup.module.scss";
import { Button, Form, Input, InputNumber, Select, Switch } from "antd";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { createPost, editPost, getPost } from "../../Services/forum";
import { twj } from "tw-to-css";
import { useEffect, useState } from "react";
import { useForm } from "antd/es/form/Form";
import { getTags } from "../../Services/tags";

function CreateGroup() {
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
      const data = await getTags({ tagType: "GENRE" });
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

  const [isSwitchOn, setSwitchOn] = useState("PUBLIC");
  const [avatarOnly, setAvatarOnly] = useState(false);


  function handleSwitchChange(checked:boolean){
    if(checked){ 
        setSwitchOn("PRIVATE");
    }else{
        setSwitchOn("PUBLIC");
    }
  };
  return (
    <div className={styles.container}>
      <Form onFinish={submit} layout="vertical" form={form}>
        <Form.Item
          name="title"
          rules={[{ required: true, message: "Please enter a title" }]}
          label="Title"
        >
          <Input placeholder="The possibilities are endless! What's the name of your squad?" />
        </Form.Item>
        <Form.Item name="tags" label="Tags">
          <Select
            allowClear
            mode="multiple"
            placeholder="You can add tags to your group."
            options={tagOptions}
          />
        </Form.Item>
        <Form.Item
          name="description"
          rules={[{ required: true, message: "Please enter description" }]}
          label="Content"
        >
          <Input.TextArea
            rows={5}
            placeholder="To describe or not to describe, that is the question."
          />
        </Form.Item>
        <Form.Item
            name="policy"
            label={"Policy: "+ isSwitchOn}
            >
            <Switch
                onChange={handleSwitchChange}
            />
        </Form.Item>
        <Form.Item
            name="avatarOnly"
            label={"Avatar Only"}
            >
            <Switch
                onChange={setAvatarOnly}
            />
        </Form.Item>
        <Form.Item
            name = "quota"
            label="Quota"
            rules={[{ required: true, message: "Number of members in your group cannot be infinite." }]}
        >
            <InputNumber 
                min={1} 
                max={10} 
                defaultValue={3} 
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

export default CreateGroup;
