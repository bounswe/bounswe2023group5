import axios from "axios";
import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";

export async function CategoryPOST(body) {
  try {
    body.userEmail = getUserEmail();
    const response = await axios.post(getAPIUrl() + "/games/category", body);
    console.log(response.data);
  } catch (error) {
    console.error(error);
  }
}

export async function CategoryGET() {
  console.log(getUserEmail());
  const response = await fetch(
    getAPIUrl() + `/games/category?userEmail=${getUserEmail()}`
  );
  return await response.json();
}
