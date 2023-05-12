import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"

const route= "/topgames";

export async function TopGamesPOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl()+"/games"+route, body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}

export async function TopGamesGET() {
    const response = await axios.get(getAPIUrl()+"/games"+ route, {params: {userEmail: getUserEmail()}});
    return await response.data;
}
