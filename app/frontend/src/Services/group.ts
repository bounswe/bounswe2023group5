import axios from "axios";

export async function getGroup(id: string) {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/group/get?id=${id}`
  );
  return response.data;
}
