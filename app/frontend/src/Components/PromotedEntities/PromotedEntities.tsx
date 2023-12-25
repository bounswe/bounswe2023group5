import { CrownFilled, TeamOutlined, UserOutlined } from "@ant-design/icons";
import styles from "./PromotedEntities.module.scss";
import { Button, Carousel, Tooltip, message } from "antd";

import { useNavigate } from "react-router-dom";
import { truncateWithEllipsis } from "../../Library/utils/truncate";
import { useElementSize } from "usehooks-ts";

function PromotedEntities({ games }: { games: any[] }) {
  const navigate = useNavigate();
  const [containerRef, { width }] = useElementSize();

  return (
    <div className={styles.container} ref={containerRef}>
      <Carousel autoplay style={{ width: `${width - 20}px` }}>
        {games.map((game: any) => (
          <div key={game.id} className={styles.gameItem}>
            <div
              className={styles.header}
              onClick={() => navigate(`/game/detail/${game.id}`)}
            >
              <h1>{game.gameName}</h1>
              <Tooltip title="Promoted Game">
                <CrownFilled className={styles.crown} />
              </Tooltip>
            </div>

            <div className={styles.content}>
              <img
                src={`${import.meta.env.VITE_APP_IMG_URL}${game?.gameIcon}`}
                alt={game.gameName}
                className={styles.icon}
                onClick={() => navigate(`/game/detail/${game.id}`)}
              ></img>
              <div className={styles.descriptionContainer}>
                <p className={styles.description}>
                  {truncateWithEllipsis(game.gameDescription, 300)}
                </p>
              </div>
            </div>
          </div>
        ))}
      </Carousel>
    </div>
  );
}

export default PromotedEntities;
