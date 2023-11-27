import { Button, Input } from "antd";
import styles from "./CreateTag.module.scss";
import { useState } from "react";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { useMutation, useQuery } from "react-query";
import { addTag, getTags } from "../../../../Services/tags";
import { SketchPicker } from "react-color";
import { NotificationUtil } from "../../../../Library/utils/notification";
import { handleAxiosError } from "../../../../Library/utils/handleError";

function CreateTag() {
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [color, setColor] = useState("#fff");

  const { data: tags } = useQuery(["tags"], getTags);

  const handleChange = (selectedColor: any) => {
    setColor(selectedColor.hex);
  };

  const addTagMutation = useMutation(addTag, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully create tag.");
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const onChange = (filterKey: string, value: string) => {
    setType(value);
  };

  const handleClick = () => {
    addTagMutation.mutate({ name, tagType: type, color });
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Create Tag</h1>

      <div className={styles.form}>
        <SingleSelect
          title="Type"
          filterKey="type"
          elements={Array.from(
            new Set(tags?.map((tag: { tagType: string }) => tag.tagType))
          )}
          reset={false}
          onChange={onChange}
          className={styles.select}
        ></SingleSelect>
        <Input
          placeholder="Name"
          value={name}
          className={styles.input}
          onChange={(event) => setName(event.target.value)}
        />
        <h4 className={styles.colorHeader}>Color</h4>
        <SketchPicker
          color={color}
          disableAlpha={true}
          onChange={handleChange}
          className={styles.picker}
          width="250px"
        />
        <Button type="primary" className={styles.button} onClick={handleClick}>
          Create Tag
        </Button>
      </div>
    </div>
  );
}

export default CreateTag;
