import { useState } from "react";
import { useMutation, useQuery } from "react-query";
import DatePicker from "react-datepicker";
import styles from "./UpdateGame.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input, Upload } from "antd";
import TextArea from "antd/es/input/TextArea";
import { InboxOutlined } from "@ant-design/icons";
import { uploadImage } from "../../../../Services/image";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleAxiosError } from "../../../../Library/utils/handleError";
import { getGames, updateGame } from "../../../../Services/games";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { getGame } from "../../../../Services/gamedetail";

function UpdateGame() {
  const [id, setId] = useState("");
  const [game, setGame] = useState(null);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [releaseDate, setReleaseDate] = useState<Date | null>(new Date());
  const [minSystemReq, setMinSystemReq] = useState("");
  const [fileList, setFileList] = useState<any[]>([]);

  const { data: games } = useQuery(["games"], () => getGames());

  const { Dragger } = Upload;
  const updateGameMutation = useMutation(updateGame, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully update the game.");
    },
    onError: (error) => {
      console.log(error);
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
    let gameIcon;
    if (fileList.length > 0) {
      console.log(fileList);
      gameIcon = await uploadImageMutation.mutateAsync(
        fileList[0].originFileObj
      );
    }

    updateGameMutation.mutate({
      id,
      name,
      description,
      releaseDate,
      gameIcon: gameIcon || game?.gameIcon,
      minSystemReq,
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

  const onGameChange = async (_filterKey: string, value: string) => {
    let updatedGame = games.find((game) => game.gameName === value);
    updatedGame = await getGame(updatedGame.id);
    setId(updatedGame.id);
    setGame(updatedGame);
    setName(updatedGame.gameName);
    setDescription(updatedGame.gameDescription);
    setMinSystemReq(updatedGame.minSystemReq);

    const dateObject = new Date(Date.parse(updatedGame.releaseDate));

    setReleaseDate(dateObject);
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Update Game</h1>
      {games?.length > 0 && (
        <SingleSelect
          className={styles.select}
          title="Game"
          elements={games.map((game) => game.gameName)}
          onChange={onGameChange}
          reset={false}
        ></SingleSelect>
      )}
      {game && (
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
            <p className="ant-upload-hint">
              You can only upload one image file.
            </p>
          </Dragger>

          <h4 className={styles.colorHeader}>Release Date</h4>
          <DatePicker
            selected={releaseDate}
            onChange={(date) => setReleaseDate(date)}
            className={styles.datePicker}
          />
          <br></br>
          <Input
            placeholder="Min System Requirements"
            value={minSystemReq}
            className={styles.input}
            onChange={(event) => setMinSystemReq(event.target.value)}
          />
          <Button className={styles.filterButton} onClick={handleClick}>
            Update Game
          </Button>
        </div>
      )}
    </div>
  );
}

export default UpdateGame;
