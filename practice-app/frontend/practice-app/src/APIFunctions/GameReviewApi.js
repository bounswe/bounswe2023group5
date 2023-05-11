import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function GameReviewPOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl()+"/games/review", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}

export async function GameReviewGET() {
    const response = await axios.get(getAPIUrl()+"/games/review", {params: {userEmail: getUserEmail()}});
    return await response.data;
}
