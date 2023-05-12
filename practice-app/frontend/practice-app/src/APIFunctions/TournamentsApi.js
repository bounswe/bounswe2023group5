import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function TournamentsPOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl() + "/games/tournaments", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }

}

export async function TournamentsGET() {

    try {
        const response = await axios.get(getAPIUrl() + "/games/tournaments", { params: { userEmail: getUserEmail() } });
        return await response.data;
    } catch (error) {
        console.error(error);
    }

}
