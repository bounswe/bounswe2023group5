import axios from "axios";
import { getGames } from "./games";

export const getAchievementByGame = async ({ gameId }: { gameId: string }) => {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/achievement/get-game-achievements",
    {
      params: {
        gameId,
      },
    }
  );
  return response.data;
};

export async function createAchievement({
  title,
  description,
  icon,
  type,
  game,
}: {
  title: string;
  description: string;
  icon: any;
  type: string;
  game: string;
}) {
  let gameId = undefined;
  if (game) {
    gameId = (await getGames(undefined, game))[0].id;
  }

  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/achievement/create`,
    { title, description, icon, type, game: gameId }
  );
  return response;
}
