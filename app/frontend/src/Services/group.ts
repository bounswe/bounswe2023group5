import axios from "axios";

export async function getGroup(id: string) {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/group/get?id=${id}`
  );
  return response.data;
}

export async function createGroup(
  title: string,
  tags: string[],
  membershipPolicy: string,
  quota: number,
  avatarOnly: boolean,
  description: string,
  gameId: string
) {
  console.log({
    title,
    tags,
    membershipPolicy,
    quota,
    avatarOnly,
    gameId,
    description,
  });
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/create`,
    {
      title,
      tags,
      membershipPolicy,
      quota,
      avatarOnly,
      gameId,
      description,
    }
  );
  return response.data;
}

export async function joinGroup(groupId: string) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/join`, null, {params: {id: groupId}});
    return response.data;
}

export async function leaveGroup(groupId: string) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/leave`, null, {params: {id: groupId}});
    return response.data;
}