import { useQuery } from "react-query";
import { getAchievementByGame } from "../../Services/achievement";
import Achievement from "../../Components/Achievement/Achievement/Achievement";
import SquareAchievement from "../../Components/Achievement/SquareAchievement/SquareAchievement";
import PromotedEntities from "../../Components/PromotedEntities/PromotedEntities";
import { getGames } from "../../Services/games";
import { useState } from "react";


function Achievements(){
    const gameId:string = "841cbf45-90cc-47b7-a763-fa3a18218bf9"

    const { data: games, isLoading: isLoadingGames } = useQuery(
        ["Games", gameId],
        () => getGames(),
        {
          onSuccess: (data) => {
            // Check if the data is an array and has at least two elements
            if (Array.isArray(data) && data.length >= 2) {
              // Update promotedEntities with the first two elements of the data array
              setPromotedEntities(data.slice(0, 2));
            }
          },
        }
      );
      
    const [promotedEntities, setPromotedEntities] = useState<any[]>([]);

    return(
        <div>
        {!isLoadingGames &&
           

                <div style={{width: "1000px"}}>
                    <PromotedEntities games={promotedEntities}/>
                </div>
                
            }
        </div>
       
    )
}

export default Achievements;