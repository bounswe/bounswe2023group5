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
  gameIcon,
  ...tags
}: {
  name: string;
  description: string;
  releaseDate: Date | null;
  gameIcon: any;
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
      gameIcon,
    }
  );

  return response.data;
};

export const updateGame = async ({
  name,
  description,
  releaseDate,
  gameIcon,
  minSystemReq,
}: {
  name: string;
  description: string;
  releaseDate: Date | null;
  gameIcon: any;
  minSystemReq: string;
}) => {
  const response = await axios.put(
    `${import.meta.env.VITE_APP_API_URL}/game/update`,
    {
      gameName: name,
      gameDescription: description,
      releaseDate,
      gameIcon,
      minSystemReq: minSystemReq || "4GB RAM", // will be changed
    }
  );

  return response.data;
};
