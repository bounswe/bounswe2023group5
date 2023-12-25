import { ArrowRightOutlined, ExportOutlined } from "@ant-design/icons";
import styles from "./RecommendationItem.module.scss";
import { Link } from "react-router-dom";

function RecommendationItem({
  image,
  name,
  link,
  index,
  showName,
}: {
  image: string;
  name: string;
  link: string;
  index?: number;
  showName?: boolean;
}) {
  return (
    <Link className={styles.container} to={link}>
      {index !== undefined && <span className={styles.index}>{index}</span>}
      {image ? (
        <img
          src={`${import.meta.env.VITE_APP_IMG_URL}${image}`}
          alt={name}
          width={120}
          height={100}
          className={styles.icon}
        ></img>
      ):
      (<img
        src={"../../../assets/images/guru.jpeg"}
        alt={name}
        width={120}
        height={100}
        className={styles.icon}
      ></img>)}
      {showName && <span className={styles.showName}>{name}</span>}
      <div className={styles.overlay}>
        {!showName && <span className={styles.name}>{name}</span>}{" "}
        <div className={styles.icon}>
          <ExportOutlined />
        </div>
      </div>
    </Link>
  );
}

export default RecommendationItem;
