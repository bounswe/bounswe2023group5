import axios from "axios";
export const getGames = async (filters?: any, search?: any) => {
  const response = await axios.post(
    "http://localhost:8080/api/game/get-game-list",
    { ...filters, search }
  );

  return response.data;
};
