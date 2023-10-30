import axios from "axios";
export const getTags = async () => {
  const response = await axios.get("http://localhost:8080/api/tag/get-all");
  return response.data;
};
