import axios from "axios";
export const getGames = async (filters?: any, search?: any) => {
  const response = await axios.post(
    import.meta.env.VITE_APP_API_URL + "/api/game/get-game-list",
    { ...filters, search }
  );

  return response.data;
};
