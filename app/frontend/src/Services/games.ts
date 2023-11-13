import axios from "axios";
export const getGames = async (filters?: any, search?: any) => {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/game/get-game-list`,
    { ...filters, search }
  );

  return response.data;
};

export const createGame = async ({
  name,
  description,
  releaseDate,
  ...tags
}: {
  name: string;
  description: string;
  releaseDate: Date | null;
  tags: any;
}) => {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/game/create`,
    {
      gameName: name,
      gameDescription: description,
      releaseDate,
      ...tags,
      platforms: tags.platform,
      minSystemReq: "4GBRAM", // will be changed
      gameIcon: "game-icon", // will be changed
    }
  );

  return response.data;
};
