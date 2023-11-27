import { CSSProperties, useState } from "react";
import SquareAchievement from "../../Achievement/SquareAchievement/SquareAchievement";
import styles from "./ProfileAchievements.module.scss";
import Achievement from "../../Achievement/Achievement/Achievement";
import { Button } from "antd";
import { twj } from "tw-to-css";
import clsx from "clsx";

function ProfileAchievements({
  style,
  achievements,
}: {
  style?: CSSProperties;
  achievements: any[];
}) {
  const [expand, setExpand] = useState(false);
  return (
    <div style={style} className={styles.container}>
      <div className={styles.btn}>
        <Button onClick={() => setExpand((t) => !t)} style={twj("w-full")}>
          {!expand ? "Detail View" : "Normal View"}
        </Button>
      </div>
      <div className={clsx(styles.achievements, expand && styles.expand)}>
        {!expand
          ? achievements.map((a) => <SquareAchievement props={a} />)
          : achievements.map((a) => <Achievement props={a} />)}
      </div>
    </div>
  );
}

export default ProfileAchievements;
