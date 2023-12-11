import styles from "./Character.module.scss";

function Character({
  onClick,
  name,
  imgUrl,
}: {
  onClick: () => void;
  name: string;
  imgUrl: string;
}) {
  return (
    <div>
      <div
        className={styles.characterContainer}
        style={{
          backgroundImage: `url(${imgUrl})`,
        }}
        onClick={() => onClick()}
      >
        <div className={styles.fadeContainer}>
          <div
            style={{
              background:
                "linear-gradient(rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0.733) 75%, rgb(0, 0, 0) 100%)",
              position: "absolute",
              width: "100%",
              height: "100%",
            }}
          >
            <div className={styles.charName}>{name}</div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Character;
