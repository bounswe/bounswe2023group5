import axios from "axios";

export async function changePassword(
  currentPassword: string,
  newPassword: string
) {
  try {
    const response = await axios.post(
      `${import.meta.env.VITE_APP_API_URL}/auth/change-password`,
      { currentPassword, newPassword }
    );
    return response;
  } catch (error) {}
}
