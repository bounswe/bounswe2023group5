import axios from "axios";
import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";


export async function ChoicePOST(body) {
    try {
      body.userEmail = getUserEmail();
      const response = await axios.post(getAPIUrl() + "/games/choice", body);
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  }
  
  export async function ChoiceGET() {
    console.log(getUserEmail());
    const response = await fetch(
      getAPIUrl() + `/games/choice?userEmail=${getUserEmail()}`
    );
    return await response.json();
  }
  