import { Select } from "antd";
import styles from "./SingleSelect.module.scss";

function SingleSelect(props: {
  title: string;
  filterKey?: string;
  defaultValue?: string;
  elements: any[] | undefined;
  reset: boolean;
  onChange: (filterKey: string, selected: string) => void;
  className?: string;
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
    props.onChange(props.filterKey || "", value);
  };

  return (
    <Select
      showSearch
      defaultValue={props.defaultValue}
      className={`${styles.container} ${props.className}`}
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

export default SingleSelect;
