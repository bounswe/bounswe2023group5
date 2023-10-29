import { StarFilled, StarOutlined } from "@ant-design/icons";
import styles from "./GameDetails.module.scss";
import { useState } from "react";
import TagRenderer from "../../Components/TagRenderer/TagRenderer";

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
      <div className={styles.subPage}>
        <TagRenderer
          tags={[
            { name: "Action", color: "#FF5733" },
            { name: "Adventure", color: "#33FF57" },
            { name: "RPG", color: "#3357FF" },
            { name: "Strategy", color: "#8D33FF" },
            { name: "Sports", color: "#FF33F6" },
            { name: "Puzzle", color: "#FFC300" },
            { name: "Simulation", color: "#058C42" },
            { name: "Horror", color: "#FF3300" },
          ]}
        />
      </div>
    </div>
  );
}

export default GameDetails;
