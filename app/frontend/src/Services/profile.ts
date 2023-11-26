import axios from "axios";

export async function editProfile({username, isPrivate, profilePhoto, steamProfile, epicGamesProfile, xboxProfile} : 
    {
        username: string,
        isPrivate: boolean,
        profilePhoto: string,
        steamProfile: string,
        epicGamesProfile: string,
        xboxProfile: string
    }) {
    await axios.post(
        import.meta.env.VITE_APP_API_URL + "/profile/edit", {
            username,
            isPrivate,
            profilePhoto,
            steamProfile,
            epicGamesProfile,
            xboxProfile
        });
}