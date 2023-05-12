import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function UserPOST(body) {

  try {
    body.userEmail = getUserEmail();
    const response = await axios.post(getAPIUrl() + "/games/user", body);
    console.log(response.data);
  } catch (error) {
    console.error(error);
  }
}

export async function UserGET() {
  const response = await axios.get(getAPIUrl() + "/games/user", { params: { userEmail: getUserEmail() } });
  return await response.data;
}
