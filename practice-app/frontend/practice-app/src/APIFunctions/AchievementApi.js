import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function AchievementPOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl()+"/games/achievement", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }

}

export async function AchievementGET() {

    try {
        const response = await axios.get(getAPIUrl()+"/games/achievement", {params: {userEmail: getUserEmail()}});
        return await response.data;
    } catch (error) {
        console.error(error);
    }
    
}
