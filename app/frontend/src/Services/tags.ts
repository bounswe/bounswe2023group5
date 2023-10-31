import axios from "axios";
export const getTags = async () => {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/tag/get-all"
  );
  return response.data;
};
