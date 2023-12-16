import axios from "axios";

export async function getProfile(id: string) {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/profile/get?userId=${id}`
  );
  return response.data;
}
export async function addGame({
  profileId,
  gameId,
}: {
  profileId: string;
  gameId: string;
}) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/profile/add-game?id=${profileId}`,
    { game: gameId }
  );
  return response.data;
}
export async function removeGame({
  profileId,
  gameId,
}: {
  profileId: string;
  gameId: string;
}) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/profile/remove-game?id=${profileId}`,
    { game: gameId }
  );
  return response.data;
}
export async function editProfile(
  {
    username,
    isPrivate,
    profilePhoto,
    steamProfile,
    epicGamesProfile,
    xboxProfile,
  }: {
    username: string;
    isPrivate: boolean;
    profilePhoto: string;
    steamProfile: string;
    epicGamesProfile: string;
    xboxProfile: string;
  },
  profileId: string
) {
  await axios.post(
    import.meta.env.VITE_APP_API_URL + "/profile/edit",
    {
      username,
      isPrivate,
      profilePhoto,
      steamProfile,
      epicGamesProfile,
      xboxProfile,
    },
    { params: { id: profileId } }
  );
}
