import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function DealPOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl()+"/games/deal", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}

export async function DealGET() {
    const response = await axios.get(getAPIUrl()+"/games/deal", {params: {userEmail: getUserEmail()}});
    return await response.data;
}
