import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./ForumPostForm.module.scss";
import { Button, Form, Input, Select } from "antd";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { createPost, editPost, getPost } from "../../Services/forum";
import { twj } from "tw-to-css";
import { useEffect, useState } from "react";
import { useForm } from "antd/es/form/Form";
import { getTags } from "../../Services/tags";
import UploadArea from "../../Components/UploadArea/UploadArea";
import { getGameAchievements } from "../../Services/achievement";
import SquareAchievement from "../../Components/Achievement/SquareAchievement/SquareAchievement";
import clsx from "clsx";
import { getCharacterByGame } from "../../Services/character";
import Character from "../../Components/Character/Character";

function ForumPostForm() {
  const [form] = useForm();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const editId = searchParams.get("editId");
  const queryClient = useQueryClient();
  const [imageUrl, setImageUrl] = useState<string | undefined>();
  const [achievement, setAchievement] = useState<any>(null);
  const [character, setCharacter] = useState<any>(null);

  const gameId = searchParams.get("gameId");

  const { data: achievements } = useQuery(
    ["achievement", gameId],
    () => getGameAchievements(gameId!),
    { enabled: !!gameId }
  );

  const { data: characters, isLoading: isLoadingCharacters } = useQuery(
    ["characters", gameId],
    () => getCharacterByGame(gameId!),
    {
      enabled: !!gameId,
    }
  );

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
          postImage: imageUrl,
          achievement: achievement || undefined,
          character: character || undefined,
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
        {!editId && (
          <>
            <Form.Item label="Image">
              <UploadArea
                style={twj("bg-gray-50 my-2")}
                onUpload={setImageUrl}
              />
            </Form.Item>

            {gameId && (
              <Form.Item label="Achievements">
                <div className={styles.achievements}>
                  {achievements?.map((a: any) => (
                    <div
                      className={clsx(achievement === a.id && styles.active)}
                      key={a?.id}
                    >
                      <SquareAchievement
                        props={a}
                        onClick={() => {
                          if (achievement === a.id) {
                            setAchievement(null);
                          } else {
                            setAchievement(a.id);
                          }
                        }}
                      />
                    </div>
                  ))}
                </div>
              </Form.Item>
            )}

            {gameId && (
              <Form.Item label="Characters">
                <div className={styles.achievements}>
                  {characters?.map((a: any) => (
                    <div
                      className={clsx(character === a.id && styles.active)}
                      key={a?.id}
                    >
                      <Character
                        imgUrl={`${import.meta.env.VITE_APP_IMG_URL}${a?.icon}`}
                        name={a?.name}
                        onClick={() => {
                          if (character === a.id) {
                            setCharacter(null);
                          } else {
                            setCharacter(a.id);
                          }
                        }}
                      />
                    </div>
                  ))}
                </div>
              </Form.Item>
            )}

            <Form.Item name="tags" label="Tags">
              <Select
                allowClear
                mode="multiple"
                placeholder="You can add tags to your post, amazing innit."
                options={tagOptions}
              />
            </Form.Item>
          </>
        )}
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
