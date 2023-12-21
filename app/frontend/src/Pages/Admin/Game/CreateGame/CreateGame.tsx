import { useState } from "react";
import { useMutation, useQuery } from "react-query";
import DatePicker from "react-datepicker";
import styles from "./CreateGame.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { getTags } from "../../../../Services/tags";
import { Button, Input, Upload } from "antd";
import TextArea from "antd/es/input/TextArea";
import MultipleSelect from "../../../../Components/MultipleSelect/MultipleSelect";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { createGame } from "../../../../Services/games";
import { InboxOutlined } from "@ant-design/icons";
import { uploadImage } from "../../../../Services/image";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleAxiosError } from "../../../../Library/utils/handleError";

function CreateGame() {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [releaseDate, setReleaseDate] = useState<Date | null>(new Date());
  const [minReq, setMinReq] = useState("");
  const [selectedTags, setSelectedTags] = useState<any>({
    playerTypes: [],
    genre: [],
    production: "",
    platform: [],
    artStyle: [],
    developer: "",
  });
  const [fileList, setFileList] = useState<any[]>([]);

  const { Dragger } = Upload;

  const { data: tags } = useQuery(["tags"], getTags);

  const onChange = (filterKey: string, value: string[] | string) => {
    if (Array.isArray(value)) {
      const tagIds: string[] = [];
      for (const val of value) {
        tagIds.push(tags.find((tag: any) => tag.name === val).id);
      }
      setSelectedTags((filters: any) => {
        return { ...filters, [filterKey]: tagIds };
      });
    } else {
      let tagId: string = "";
      tagId = tags.find((tag: any) => tag.name === value).id;
      setSelectedTags((filters: any) => {
        return { ...filters, [filterKey]: tagId };
      });
    }
  };

  const addGameMutation = useMutation(createGame, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully create game.");
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const uploadImageMutation = useMutation(
    (i: any) => uploadImage(i, "game-icons"),
    {
      onError: (error) => {
        handleAxiosError(error);
      },
    }
  );
  const handleClick = async () => {
    const gameIcon = await uploadImageMutation.mutateAsync(
      fileList[0].originFileObj
    );

    addGameMutation.mutate({
      name,
      description,
      releaseDate,
      gameIcon,
      minReq,
      ...selectedTags,
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
        NotificationUtil.error("You can only upload image files!");
        setFileList([]);
      } else {
        setFileList([file]);
      }
    });
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Create Game</h1>

      <div className={styles.form}>
        <Input
          placeholder="Name"
          value={name}
          className={styles.input}
          onChange={(event) => setName(event.target.value)}
        />
        <TextArea
          showCount
          value={description}
          maxLength={1000}
          className={styles.input}
          onChange={(event) => setDescription(event.target.value)}
          placeholder="Description"
        />

        <h4 className={styles.colorHeader}>Game Image</h4>
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

        <h4 className={styles.colorHeader}>Release Date</h4>
        <DatePicker
          selected={releaseDate}
          onChange={(date) => setReleaseDate(date)}
          className={styles.datePicker}
        />
        <br></br>
        <Input
          placeholder="Min Requirements"
          value={minReq}
          className={styles.input}
          onChange={(event) => setMinReq(event.target.value)}
        />
        <MultipleSelect
          title="Player Types"
          filterKey="playerTypes"
          elements={tags
            ?.filter(
              (tag: { tagType: string }) => tag.tagType === "PLAYER_TYPE"
            )
            .map((elem: { name: string }) => elem.name)}
          reset={false}
          onChange={onChange}
        ></MultipleSelect>
        <MultipleSelect
          title="Genre"
          filterKey="genre"
          elements={tags
            ?.filter((tag: { tagType: string }) => tag.tagType === "GENRE")
            .map((elem: { name: string }) => elem.name)}
          reset={false}
          onChange={onChange}
        ></MultipleSelect>
        <SingleSelect
          title="Production"
          filterKey="production"
          elements={tags
            ?.filter((tag: { tagType: string }) => tag.tagType === "PRODUCTION")
            .map((elem: { name: string }) => elem.name)}
          reset={false}
          onChange={onChange}
        ></SingleSelect>
        <MultipleSelect
          title="Platform"
          filterKey="platform"
          elements={tags
            ?.filter((tag: { tagType: string }) => tag.tagType === "PLATFORM")
            .map((elem: { name: string }) => elem.name)}
          reset={false}
          onChange={onChange}
        ></MultipleSelect>
        <MultipleSelect
          title="Art Style"
          filterKey="artStyle"
          elements={tags
            ?.filter((tag: { tagType: string }) => tag.tagType === "ART_STYLE")
            .map((elem: { name: string }) => elem.name)}
          reset={false}
          onChange={onChange}
        ></MultipleSelect>
        <SingleSelect
          title="Developer"
          filterKey="developer"
          elements={tags
            ?.filter((tag: { tagType: string }) => tag.tagType === "DEVELOPER")
            .map((elem: { name: string }) => elem.name)}
          reset={false}
          onChange={onChange}
        ></SingleSelect>

        <Button className={styles.filterButton} onClick={handleClick}>
          Create Game
        </Button>
      </div>
    </div>
  );
}

export default CreateGame;
