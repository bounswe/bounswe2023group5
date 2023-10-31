import axios from "axios";

export async function getGame(id: string) {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/game/get-game?gameId=" + id
  );
  return response.data?.game;
}
