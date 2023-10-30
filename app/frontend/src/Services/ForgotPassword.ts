import axios from "axios";

async function postEmail  (email:string) {

  try {
    const response = await axios.post(`${import.meta.env.VITE_APP_API_URL}/auth/forgot-password`, {email}, {
      withCredentials: true,
    });
    return response;
  } catch (error) {
    throw error;
  }
}


async function postPassword (newPassword:any) {

  try {
    const response = await axios.post(`${import.meta.env.VITE_APP_API_URL}/auth/change-forgot-password`, newPassword, {
      withCredentials: true,
    });
    return response;
  } catch (error) {
    throw error;
  }

  }


async function postCode (data:any) {

  try {
    const response = await axios.post(`${import.meta.env.VITE_APP_API_URL}/auth/verify-reset-code`, data, {
      withCredentials: true,
    });
    return response;
  } catch (error) {
    throw error;
  }

}
export {postPassword, postCode, postEmail};
