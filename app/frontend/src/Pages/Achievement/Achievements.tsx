import { useQuery } from "react-query";
import { getAchievementByGame } from "../../Services/achievement";
import Achievement from "../../Components/Achievement/Achievement/Achievement";
import SquareAchievement from "../../Components/Achievement/SquareAchievement/SquareAchievement";


function Achievements(){
    const gameId:string = "841cbf45-90cc-47b7-a763-fa3a18218bf9"

    const { data: achievements, isLoading: isLoadingAchievements } = useQuery(
        ["achievements", gameId],
        () => getAchievementByGame({ gameId: gameId! })
      );
    
    

    return(
        <div>
            {!isLoadingAchievements &&
            achievements.map(
            (achievement: any) =>
                !achievement.isDeleted && (
                <SquareAchievement props={achievement} key={achievement.id} />
                )
            )}
        </div>
    )
}

export default Achievements;