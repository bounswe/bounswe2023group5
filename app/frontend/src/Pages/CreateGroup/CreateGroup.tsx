import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./CreateGroup.module.scss";
import { Button, Form, Input, InputNumber, Select, Switch } from "antd";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { twj } from "tw-to-css";
import { useEffect, useState } from "react";
import { useForm } from "antd/es/form/Form";
import { getTags } from "../../Services/tags";
import FormItem from "antd/es/form/FormItem";
import { getGames } from "../../Services/games";
import { createGroup } from "../../Services/group";
import UploadArea from "../../Components/UploadArea/UploadArea";

function CreateGroup() {
  const [form] = useForm();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const editId = searchParams.get("editId");
  const queryClient = useQueryClient();

  const { data: tagOptions } = useQuery(
    ["tagOptions", "forumPost"],
    async () => {
      const data = await getTags({ tagType: "GROUP" });
      return data.map((item: { name: any; id: any }) => ({
        label: item.name,
        value: item.id,
      }));
    }
  );
  const [imageUrl, setImageUrl] = useState("");
  const [membershipPolicy, setMembershipPolicy] = useState("PUBLIC");
  const [avatarOnly, setAvatarOnly] = useState(false);

  const { mutate: submit, isLoading } = useMutation(
    ({
      title,
      tags,
      quota,
      description,
      gameId,
      imageUrl,
    }: {
      title: string;
      tags: string[];
      quota: number;
      description: string;
      gameId: string;
      imageUrl: string;
    }) => {
      return createGroup(
        title,
        tags,
        membershipPolicy,
        quota,
        description,
        gameId,
        imageUrl,
      );
    },
    {
      onSuccess(data: any) {
        const groupId = data.id;
        queryClient.invalidateQueries(["groups"]);
        navigate(`/group/detail/${groupId}`);
      },
    }
  );



  const gameList = useQuery(["games"], () => getGames());

  function handleSwitchChange(checked: boolean) {
    if (checked) {
      setMembershipPolicy("PRIVATE");
    } else {
      setMembershipPolicy("PUBLIC");
    }
  }

  useEffect(() => {
    form.setFieldsValue({
      membershipPolicy: membershipPolicy,
      quota: 3,
    });
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.row}>
        <UploadArea style={twj("aspect-square")} onUpload={setImageUrl} />
      </div>
      <Form onFinish={submit} layout="vertical" form={form}>
        <Form.Item
          name="title"
          rules={[{ required: true, message: "Please enter a title" }]}
          label="Title"
        >
          <Input placeholder="The possibilities are endless! What's the name of your squad?" />
        </Form.Item>
        <FormItem
          name="gameId"
          rules={[{ required: true, message: "Please enter a game" }]}
          label="Game"
        >
          <Select
            showSearch
            placeholder="Add your game"
            optionFilterProp="children"
            filterOption={(input: string, option) =>
              (option?.label ?? "").toLowerCase().includes(input)
            }
            filterSort={(optionA: any, optionB: any) =>
              String(optionA?.gameName ?? "")
                .toLowerCase()
                .localeCompare(String(optionB?.gameName ?? "").toLowerCase())
            }
            options={gameList.data?.map(
              (item: { gameName: string; id: string }) => ({
                label: item.gameName,
                value: item.id,
              })
            )}
          />
        </FormItem>
        <Form.Item name="tags" label="Tags">
          <Select
            allowClear
            mode="multiple"
            placeholder="You can add tags to your group."
            optionFilterProp="children"
            filterOption={(input: string, option) =>
              (option?.label ?? "").toLowerCase().includes(input)
            }
            filterSort={(optionA: any, optionB: any) =>
              String(optionA?.label ?? "")
                .toLowerCase()
                .localeCompare(String(optionB?.label ?? "").toLowerCase())
            }
            options={tagOptions}
          />
        </Form.Item>
        <Form.Item
          name="description"
          rules={[{ required: true, message: "Please enter description" }]}
          label="Description"
        >
          <Input.TextArea
            rows={5}
            placeholder="To describe or not to describe, that is the question."
          />
        </Form.Item>
        <div className={styles.row}>
          <Form.Item
            name="membershipPolicy"
            label={"Policy: " + membershipPolicy}
          >
            <Switch onChange={handleSwitchChange} />
          </Form.Item>

          <Form.Item
            name="quota"
            label="Quota"
            rules={[
              {
                required: true,
                message: "Number of members in your group cannot be infinite.",
              },
            ]}
          >
            <InputNumber min={1} max={10} defaultValue={3} />
          </Form.Item>
        </div>

        <Form.Item noStyle>
          <Button
            type="primary"
            htmlType="submit"
            disabled={isLoading}
            style={twj("ml-[85%]")}
          >
            Create
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default CreateGroup;
