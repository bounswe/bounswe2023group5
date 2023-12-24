import clsx from "clsx";
import styles from "./Character.module.scss";

function Character({
  onClick,
  name,
  imgUrl,
  className,
}: {
  onClick?: () => void;
  name: string;
  imgUrl: string;
  className?: string;
}) {
  return (
    <div>
      <div
        className={clsx(styles.characterContainer, className)}
        style={{
          backgroundImage: `url(${imgUrl})`,
        }}
        onClick={() => onClick?.()}
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
