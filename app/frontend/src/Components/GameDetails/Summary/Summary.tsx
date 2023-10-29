import { Typography } from "antd";
import TagRenderer from "../../TagRenderer/TagRenderer";
import styles from "./Summary.module.scss";

function Summary() {
  return (
    <div className={styles.summaryContainer}>
      <div className={styles.fieldContainer}>
        <div className={styles.tagContainer}>
          Genre:
          <TagRenderer
            tags={[
              { name: "Action", color: "#FF5733" },
              { name: "Adventure", color: "#33FF57" },
              { name: "RPG", color: "#3357FF" },
            ]}
          />
        </div>
        <div className={styles.tagContainer}>
          Production:
          <TagRenderer tags={[{ name: "AAA", color: "#FF5733" }]} />
        </div>
        <div className={styles.tagContainer}>
          Player Type:
          <TagRenderer
            tags={[
              { name: "Single Player", color: "#F7462e" },
              { name: "Multi Player", color: "#Fce1b1" },
            ]}
          />
        </div>
        <div className={styles.tagContainer}>
          Duration:
          <TagRenderer
            tags={[
              { name: "Too long", color: "#F7462e" },
              { name: "Lifetime", color: "#Fce1b1" },
            ]}
          />
        </div>
        <div className={styles.tagContainer}>
          Art Style:
          <TagRenderer
            tags={[
              { name: "Pixel Art", color: "#F7572e" },
              { name: "Cartoony", color: "#Fc9861" },
            ]}
          />
        </div>
        <div className={styles.tagContainer}>
          Platform:
          <TagRenderer tags={[{ name: "Wooden Table", color: "#F74374e" }]} />
        </div>
        <div className={styles.tagContainer}>
          Developer:
          <TagRenderer tags={[{ name: "Mojang", color: "#abcdef" }]} />
        </div>
      </div>
      <div className={styles.summary}>
        <Typography>
          Minecraft is a 3D sandbox adventure game developed by Mojang Studios
          where players can interact with a fully customizable three-dimensional
          world made of blocks and entities. Its diverse gameplay options allow
          players to choose the way they play, creating countless possibilities.
          There are three actively maintained editions of Minecraft: Java
          Edition (PC); Bedrock Edition (Windows, mobile, and consoles); and
          Minecraft Education (for educational settings). There is also a
          localized release of the game for mainland China.
        </Typography>
      </div>
      <div className={styles.req}>
        <span>Min System Req: </span>CPU: Info CPU SPEED: 2.0 GHz multi-core
        RAM: 8 GB VIDEO CARD: 1 GB VRAM DX10 compatible DEDICATED VIDEO RAM: 1
        GB PIXEL SHADER: 4.0 VERTEX SHADER: 4.0 OS: Windows 7 64-bit SP1 FREE
        DISK SPACE: 350 MB SOUND CARD: Yes
      </div>
    </div>
  );
}

export default Summary;
