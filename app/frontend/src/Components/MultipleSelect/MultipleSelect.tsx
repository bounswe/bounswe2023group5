import { Select } from "antd";
import styles from "./MultipleSelect.module.css";

function MultipleSelect(props: {
  title: string;
  filterKey: string;
  elements: any[] | undefined;
  reset: boolean;
  onChange: (filterKey: string, selected: string) => void;
}) {
  const options =
    props.elements &&
    props.elements.map((elem) => {
      return {
        value: elem,
        label: elem,
      };
    });

  const handleChange = (value: string) => {
    props.onChange(props.filterKey, value);
  };

  return (
    <Select
      showSearch
      className={styles.container}
      mode="multiple"
      placeholder={props.title}
      optionFilterProp="children"
      onChange={handleChange}
      filterOption={(input, option) => (option?.label ?? "").includes(input)}
      filterSort={(optionA, optionB) =>
        (optionA?.label ?? "")
          .toLowerCase()
          .localeCompare((optionB?.label ?? "").toLowerCase())
      }
      options={options}
    />
  );
}

export default MultipleSelect;
