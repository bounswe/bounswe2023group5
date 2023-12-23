import { TeamOutlined, UserOutlined } from "@ant-design/icons";
import styles from "./PromotedEntities.module.scss";
import { Button, Carousel, message } from "antd";

import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "react-query";
import { joinGroup, leaveGroup } from "../../Services/group";
import { truncateWithEllipsis } from "../../Library/utils/truncate";
import GameConsole from "../../../assets/images/game-console.png";


function PromotedEntities({games}: {games: any[]}) {
  const navigate = useNavigate();
  const queryClient = useQueryClient();



  return (
    
     (
      <div className={styles.container}>
        

        <Carousel autoplay>
            {
                games.map((game: any) => (
                    <div key={game.id} className={styles.gameItem} >
                        <div className={styles.header} onClick={() => navigate(`/game/detail/${game.id}`)}>
                            <h1>{game.gameName}</h1>
   
                            <h1 className={styles.promotion}>{ "(PROMOTED)"}</h1>
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
                ))
            }
        </Carousel>

        
      </div>
    )
)

}

export default PromotedEntities;
