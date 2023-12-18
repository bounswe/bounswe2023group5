import { useState } from "react";
import { useMutation, useQuery } from "react-query";
import styles from "./CreateCharacter.module.scss";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input, Upload } from "antd";
import MultipleSelect from "../../../../Components/MultipleSelect/MultipleSelect";
import { InboxOutlined } from "@ant-design/icons";
import { uploadImage } from "../../../../Services/image";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleAxiosError } from "../../../../Library/utils/handleError";
import { createGame, getGames } from "../../../../Services/games";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { createCharacter } from "../../../../Services/character";

function CreateCharacter() {
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [gender, setGender] = useState("");
  const [race, setRace] = useState("");
  const [status, setStatus] = useState("");
  const [occupation, setOccupation] = useState("");
  const [birthDate, setBirthDate] = useState<Date | null>(new Date());
  const [voiceActor, setVoiceActor] = useState("");
  const [height, setHeight] = useState("");
  const [age, setAge] = useState("");
  const [characterGames, setCharacterGames] = useState<string[]>([]);

  const [fileList, setFileList] = useState<any[]>([]);

  const { data: games } = useQuery(["games"], () => getGames());

  const { Dragger } = Upload;

  const addCharacterMutation = useMutation(createCharacter, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully create character.");
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const uploadImageMutation = useMutation(
    (i: any) => uploadImage(i, "character-icons"),
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
    const gameIds = characterGames.map((gameName) => {
      return games.find((game) => game.gameName === gameName).id;
    });

    addCharacterMutation.mutate({
      name,
      type,
      gender,
      race,
      status,
      occupation,
      birthDate: birthDate as Date,
      voiceActor,
      height,
      age,
      games: gameIds,
      icon: gameIcon,
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

  const onGameChange = async (_filterKey: string, value: string[]) => {
    setCharacterGames(value);
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Create Character</h1>

      <div className={styles.form}>
        <Input
          placeholder="Name"
          value={name}
          className={styles.input}
          onChange={(event) => setName(event.target.value)}
        />
        <h4 className={styles.colorHeader}>Character Image</h4>
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
        <br></br>
        {games?.length > 0 && (
          <MultipleSelect
            title="Games"
            filterKey="games"
            elements={games.map((game) => game.gameName)}
            reset={false}
            onChange={onGameChange}
          ></MultipleSelect>
        )}
        <br></br>
        <h4 className={styles.colorHeader}>Birth Date</h4>
        <DatePicker
          selected={birthDate}
          onChange={(date) => setBirthDate(date)}
          className={styles.datePicker}
        />
        <br></br>
        <br></br>
        <br></br>
        <Input
          placeholder="Type"
          value={type}
          className={styles.input}
          onChange={(event) => setType(event.target.value)}
        />
        <Input
          placeholder="Gender"
          value={gender}
          className={styles.input}
          onChange={(event) => setGender(event.target.value)}
        />
        <Input
          placeholder="Race"
          value={race}
          className={styles.input}
          onChange={(event) => setRace(event.target.value)}
        />
        <Input
          placeholder="Status"
          value={status}
          className={styles.input}
          onChange={(event) => setStatus(event.target.value)}
        />
        <Input
          placeholder="Occupation"
          value={occupation}
          className={styles.input}
          onChange={(event) => setOccupation(event.target.value)}
        />

        <Input
          placeholder="Voice Actor"
          value={voiceActor}
          className={styles.input}
          onChange={(event) => setVoiceActor(event.target.value)}
        />
        <Input
          placeholder="Height"
          value={height}
          className={styles.input}
          onChange={(event) => setHeight(event.target.value)}
        />
        <Input
          placeholder="Age"
          value={age}
          className={styles.input}
          onChange={(event) => setAge(event.target.value)}
        />

        <Button className={styles.filterButton} onClick={handleClick}>
          Create Character
        </Button>
      </div>
    </div>
  );
}

export default CreateCharacter;
