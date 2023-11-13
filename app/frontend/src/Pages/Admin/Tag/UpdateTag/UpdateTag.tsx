import { Button, Input } from "antd";
import styles from "./UpdateTag.module.scss";
import { useEffect, useState } from "react";
import SingleSelect from "../../../../Components/SingleSelect/SingleSelect";
import { useMutation, useQuery } from "react-query";
import { updateTag, getTags, getTagById } from "../../../../Services/tags";
import { SketchPicker } from "react-color";

function UpdateTag() {
  const [name, setName] = useState("");
  const [updatedName, setUpdatedName] = useState("");
  const [updatedType, setUpdatedType] = useState("");
  const [updatedColor, setUpdatedColor] = useState("");

  const { data: tags } = useQuery(["tags"], getTags);

  const { Search } = Input;

  const { data: updatedTag } = useQuery(["updatedTags", name, tags], () => {
    if (tags) {
      const id = tags.find((tag) => tag.name === name).id;
      return getTagById(id);
    }
  });

  useEffect(() => {
    if (updatedTag) {
      setUpdatedName(updatedTag.name);
      setUpdatedType(updatedTag.tagType);
      setUpdatedColor(updatedTag.color);
    }
  }, [updatedTag]);

  const handleChange = (selectedColor: any) => {
    setUpdatedColor(selectedColor.hex);
  };

  const updateTagMutation = useMutation(updateTag, {
    onSuccess: async () => {
      alert("You successfully update tag.");
    },
    onError: () => {
      alert("Something went wrong");
    },
  });

  const onChange = (filterKey: string, value: string) => {
    setUpdatedType(value);
  };

  const handleClick = () => {
    const id = tags.find((tag) => tag.name === name).id;
    updateTagMutation.mutate({
      id,
      name: updatedName,
      tagType: updatedType,
      color: updatedColor,
    });
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.header}>Update Tag</h1>
      <div className={styles.form}>
        <div className={styles.search}>
          <Search
            placeholder="Enter the name of the tag you want to update"
            value={name}
            onChange={(event) => setName(event.target.value)}
          />
        </div>

        {updatedName && (
          <>
            <h3 className={styles.applyChange}>Apply Changes</h3>

            <SingleSelect
              title="Type"
              filterKey="type"
              elements={Array.from(
                new Set(tags?.map((tag: { tagType: string }) => tag.tagType))
              )}
              reset={false}
              defaultValue={updatedType}
              onChange={onChange}
              className={styles.select}
            ></SingleSelect>
            <Input
              placeholder="Name"
              value={updatedName}
              className={styles.input}
              onChange={(event) => setUpdatedName(event.target.value)}
            />
            <h4 className={styles.colorHeader}>Color</h4>
            <SketchPicker
              color={updatedColor}
              disableAlpha={true}
              onChange={handleChange}
              className={styles.picker}
              width="250px"
            />
            <Button
              type="primary"
              className={styles.button}
              onClick={handleClick}
            >
              Update Tag
            </Button>
          </>
        )}
      </div>
    </div>
  );
}

export default UpdateTag;
