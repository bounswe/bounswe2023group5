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

const data = {
  _id: "sfdghgafsgsdh",
  gameName: "Path of Exile",
  gameDescription:
    "You are an Exile, struggling to survive on the dark continent of Wraeclast, as you fight to earn power that will allow you to exact your revenge against those who wronged you. Created by hardcore gamers, Path of Exile is an online Action RPG set in a dark fantasy world. With a focus on visceral action combat, powerful items and deep character customization, Path of Exile is completely free and will never be pay-to-win.",
  gameIcon:
    "data:image/jpeg;base64,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",
  playerTypes: [
    {
      _id: "e2242792-2947-4874-94ff-f9d96df0d9d5",
      name: "Single Player",
      tagType: "PLAYER_TYPE",
      color: "#FF0000",
      createdAt: ISODate("2023-10-28T10:59:58.765+0000"),
      isDeleted: false,
    },
  ],
  genre: [
    {
      _id: "53afbdf5-5118-4d42-9793-73ac2c47238d",
      name: "MOBA",
      tagType: "GENRE",
      color: "#e95d00",
      createdAt: ISODate("2023-10-28T12:54:57.087+0000"),
      isDeleted: false,
      _class: "Tag",
    },
  ],
  platforms: [],
  artStyles: [],
  otherTags: [],
  isDeleted: false,
  _class: "com.app.gamereview.model.Game",
};

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
          <img src={`${data.gameIcon}`} alt="Alperen Çiseli Seviyo" />
        </div>
        <div className={styles.titleContainer}>
          <div className={styles.name}>
            <h1>{data.gameName}</h1>
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
        <Summary description={data.gameDescription} />
      </div>
    </div>
  );
}

export default GameDetails;
