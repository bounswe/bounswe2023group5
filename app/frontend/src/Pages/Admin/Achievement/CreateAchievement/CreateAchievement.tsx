import { useState } from "react";
import { useMutation } from "react-query";
import styles from "./CreateAchievement.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input, Upload } from "antd";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { InboxOutlined } from "@ant-design/icons";
import { uploadImage } from "../../../../Services/image";
import { createAchievement } from "../../../../Services/achievement";
import { getGames } from "../../../../Services/games";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleError } from "../../../../Library/utils/handleError";

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
      NotificationUtil.success("You successfully create achievement.");
    },
    onError: (error) => {
      handleError(error);
    },
  });

  const { data: games } = useQuery(["games"], () => getGames());

  const onChangeType = (_filterKey: string, value: string) => {
    setType(value as string);
  };

  const onChangeGame = (_filterKey: string, value: string) => {
    setGame(value as string);
  };

  const uploadImageMutation = useMutation(uploadImage, {
    onError: (error) => {
      handleError(error);
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

    fileList?.map((file) => {
      if (file.type.indexOf("image") === -1) {
        NotificationUtil.error("You can only upload image files!");
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
          onChange={onChangeType}
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

        <SingleSelect
          className={styles.select}
          title="Game"
          elements={games?.map((game) => game.gameName)}
          onChange={onChangeGame}
          reset={false}
        ></SingleSelect>
        <br></br>

        <Button className={styles.filterButton} onClick={handleClick}>
          Create Achievement
        </Button>
      </div>
    </div>
  );
}

export default CreateAchievement;
