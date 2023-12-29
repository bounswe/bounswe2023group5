import axios from "axios";

export async function getGameRecommendations() {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/game/get-recommendations`
  );

  return response.data;
}

export async function getGroupRecommendations() {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/group/get-recommendations`
  );

  return response.data;
}
