import axios from "axios";

export async function getHomePosts() {
  const response = await axios.get(import.meta.env.VITE_APP_API_URL + "/home");
  return response?.data;
}
