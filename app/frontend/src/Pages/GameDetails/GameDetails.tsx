import { StarFilled, StarOutlined } from "@ant-design/icons";
import styles from "./GameDetails.module.scss";
import { useState } from "react";
import TagRenderer from "../../Components/TagRenderer/TagRenderer";
import Summary from "../../Components/GameDetails/Summary/Summary";
import { useParams } from "react-router-dom";

function formatDate(date: Date) {
  const day = date.getDate();
  const monthIndex = date.getMonth();
  const year = date.getFullYear();

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const monthName = monthNames[monthIndex];

  return `${day} ${monthName} ${year}`;
}

function GameDetails() {
  const { gameId } = useParams();
  console.log(gameId);
  const score = 4;
  const [subPage, setSubPage] = useState<"summary" | "reviews" | "forum">(
    "summary"
  );

  const date = new Date();
  return (
    <div className={styles.container}>
      <div className={styles.info}>
        <div className={styles.pictureContainer}>
          <img src="./placeholders/minecraft.jpg" alt="Alperen Çiseli Seviyo" />
        </div>
        <div className={styles.titleContainer}>
          <div className={styles.name}>
            <h1>Minecraft: Java Edition</h1>
            <span>{formatDate(date)}</span>
          </div>
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
            key={name}
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
        <Summary />
      </div>
    </div>
  );
}

export default GameDetails;
