import axios from "axios";

export async function getHomePosts(
  sortDirection?: "ASCENDING" | "DESCENDING",
  sortBy?: "CREATION_DATE" | "OVERALL_VOTE" | "VOTE_COUNT"
) {
  const response = await axios.get(import.meta.env.VITE_APP_API_URL + "/home", {
    params: { sortBy, sortDirection },
  });
  return response?.data;
}
