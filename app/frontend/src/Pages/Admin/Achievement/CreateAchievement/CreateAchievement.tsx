import { useState } from "react";
import { useMutation } from "react-query";
import styles from "./CreateAchievement.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input, Upload } from "antd";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { InboxOutlined } from "@ant-design/icons";
import { uploadImage } from "../../../../Services/image";
import { createAchievement } from "../../../../Services/achievement";

function CreateAchievement() {
  const ACHIEVEMENT_TYPES = ["GAME", "META"];
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [game, setGame] = useState("");
  const [type, setType] = useState("");

  const [fileList, setFileList] = useState<any[]>([]);

  const { Dragger } = Upload;

  const createAchievementMutation = useMutation(createAchievement, {
    onSuccess: async () => {
      alert("You successfully create achievement.");
    },
    onError: () => {
      alert("Something went wrong");
    },
  });

  const onChange = (_filterKey: string, value: string[] | string) => {
    setType(value as string);
  };

  const uploadImageMutation = useMutation(uploadImage, {
    onError: () => {
      alert("We cannot upload the image");
    },
  });
  const handleClick = async () => {
    const icon = await uploadImageMutation.mutateAsync(
      fileList[0].originFileObj
    );

    createAchievementMutation.mutate({
      title,
      description,
      icon,
      type,
      game,
    });
  };

  const handleChange = async (info: any) => {
    let fileList = [...info.fileList];

    fileList = fileList.slice(-1);

    if (fileList.length === 0) {
      setFileList([]);
      return;
    }

    fileList.map((file) => {
      if (file.type.indexOf("image") === -1) {
        alert("You can only upload image files!");
        setFileList([]);
      } else {
        setFileList([file]);
      }
    });
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Create Achievement</h1>

      <div className={styles.form}>
        <Input
          placeholder="Title"
          value={title}
          className={styles.input}
          onChange={(event) => setTitle(event.target.value)}
        />
        <Input
          placeholder="Description"
          value={description}
          className={styles.input}
          onChange={(event) => setDescription(event.target.value)}
        />
        <SingleSelect
          title="Type"
          filterKey="type"
          elements={ACHIEVEMENT_TYPES}
          reset={false}
          onChange={onChange}
        ></SingleSelect>

        <h4 className={styles.colorHeader}>Achievement Icon</h4>
        <Dragger
          fileList={fileList}
          beforeUpload={() => false}
          onChange={handleChange}
          className={styles.dragger}
        >
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">
            Click or drag an image file to this area to upload
          </p>
          <p className="ant-upload-hint">You can only upload one image file.</p>
        </Dragger>

        <Input
          placeholder="Game"
          value={game}
          className={styles.inputWithMargin}
          onChange={(event) => setGame(event.target.value)}
        />

        <Button className={styles.filterButton} onClick={handleClick}>
          Create Achievement
        </Button>
      </div>
    </div>
  );
}

export default CreateAchievement;
