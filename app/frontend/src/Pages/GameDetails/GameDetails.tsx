import { StarFilled, StarOutlined } from "@ant-design/icons";
import styles from "./GameDetails.module.scss";
import { useState } from "react";

function GameDetails() {
  const score = 4;
  const [subPage, setSubPage] = useState<"summary" | "reviews" | "forum">(
    "summary"
  );
  return (
    <div className={styles.container}>
      <div className={styles.info}>
        <div className={styles.pictureContainer}>
          <img src="./placeholders/minecraft.jpg" alt="Alperen Çiseli Seviyo" />
        </div>
        <div className={styles.titleContainer}>
          <h1>Minecraft: Java Edition</h1>
          <div className={styles.starsContainer}>
            {[1, 1, 1, 1, 1].map((_, index) =>
              index <= score - 1 ? (
                <StarFilled key={index} />
              ) : (
                <StarOutlined key={index} />
              )
            )}
          </div>
        </div>
      </div>
      <div className={styles.menu}>
        {["summary", "reviews", "forum"].map((name) => (
          <button
            type="button"
            className={subPage === name ? styles.active : ""}
            onClick={() => setSubPage(name as any)}
            disabled
          >
            {name}
          </button>
        ))}
      </div>
      <div className={styles.subPage}>Info</div>
    </div>
  );
}

export default GameDetails;
