import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function YugiohcardPOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl() + "/games/yugiohcard", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }

}

export async function YugiohcardGET() {

    try {
        const response = await axios.get(getAPIUrl() + "/games/yugiohcard", { params: { userEmail: getUserEmail() } });
        return await response.data;
    } catch (error) {
        console.error(error);
    }

}
