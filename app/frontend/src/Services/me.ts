import axios from "axios";

export async function me() {
  return await axios.get(import.meta.env.VITE_APP_API_URL + "/me");
}
