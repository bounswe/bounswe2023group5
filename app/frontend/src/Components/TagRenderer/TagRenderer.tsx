import styles from "./TagRenderer.module.scss";
import clsx from "clsx";
// Define the type for a single tag
interface Tag {
  name: string;
  color: string;
}

// Define the type for the component props
interface TagRendererProps {
  tags: Tag[];
  className?: string;
}

// Create the TagRenderer component
function TagRenderer({ tags, className }: TagRendererProps) {
  return (
    <div className={clsx(styles.container, className)}>
      {tags.map((tag, index) => (
        <span
          key={index}
          className={styles.tag}
          style={{
            backgroundColor: tag.color ?? "#FFFFFF",
          }}
        >
          {tag.name}
        </span>
      ))}
    </div>
  );
}

export default TagRenderer;
