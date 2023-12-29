import axios from "axios";

export const getActivities = async () => {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/profile/get-activites"
  );
  return response.data;
};
