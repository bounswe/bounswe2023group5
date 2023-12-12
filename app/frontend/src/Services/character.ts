import axios from "axios";

export async function getCharacterByGame(gameId: string) {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/character/get-game-characters`,
    { params: { gameId: gameId } }
  );
  return response.data;
}
