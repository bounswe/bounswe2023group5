import axios from "axios";

export const getUserByUsername = async (username: string) => {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/user/get-all?username=${username}`
  );
  return response.data;
};

export const banUser = async (username: string) => {
  const userId = (await getUserByUsername(username))[0].id;

  const response = await axios.delete(
    `${import.meta.env.VITE_APP_API_URL}/user/delete?id=${userId}`
  );
  return response.data;
};

export const giveAdminPermission = async (username: string) => {
  const userId = (await getUserByUsername(username))[0].id;

  const response = await axios.put(
    `${import.meta.env.VITE_APP_API_URL}/user/change-role?id=${userId}`,
    {
      userRole: "ADMIN",
    }
  );
  return response.data;
};
