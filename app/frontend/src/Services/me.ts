import axios from "axios";

export async function me() {
  return await axios.post(`${import.meta.env.VITE_APP_API_URL}/auth/me`);
}
