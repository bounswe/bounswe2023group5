import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"


export async function GenrePOST(body) {

    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl()+"/games/genre", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}

export async function GenreGET() {
    const response = await axios.get(getAPIUrl()+"/games/genre", {params: {userEmail: getUserEmail()}});
    return await response.data;
}
