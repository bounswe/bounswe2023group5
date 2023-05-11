import { getAPIUrl, getUserEmail } from "../Utilities/utilityfunctions";
import axios from "axios"

export async function SuggestionPost(body) {
    try {
        body.userEmail = getUserEmail();
        const response = await axios.post(getAPIUrl()+"/games/card", body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}

export async function SuggestionGet() {
    const response = await fetch(getAPIUrl()+ `/games/card?userEmail=${getUserEmail()}`)
    return await response.json()
}