import axios from "axios";
async function postLogin(formData: any){

  try {
    const response = await axios.post(`${import.meta.env.VITE_APP_API_URL}/auth/register`, formData, {
      withCredentials: true,
    });
    return response;
  } catch (error) {
    throw error;
  }
};

export default postLogin;

