import axios from "axios";

export async function getProfile(id: string) {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/profile/get?userId=${id}`
  );
  return response.data;
}
