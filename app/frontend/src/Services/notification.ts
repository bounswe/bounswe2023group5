import axios from "axios";

export const getNotifications = async ({ userId }: { userId: string }) => {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/notification/get-notifications",
    {
      params: {
        userId,
      },
    }
  );
  return response.data;
};