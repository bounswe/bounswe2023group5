import axios from "axios";
import { getGames } from "./games";
import Search from "antd/es/input/Search";

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
  const gameId = (await getGames(undefined, game))[0].id;
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/achievement/create`,
    { title, description, icon, type, game: gameId }
  );
  return response;
}

export async function deleteAchievementByName({
  title,
  game,
}: {
  title: string;
  game: string;
}) {
  const response = await axios.delete(
    `${
      import.meta.env.VITE_APP_API_URL
    }/achievement/delete-by-name?achievementName=${title}&gameName=${game}`
  );
  return response;
}
