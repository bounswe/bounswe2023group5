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
  description: string,
  gameId: string,
  imageUrl: string
) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/create`,
    {
      title,
      tags,
      membershipPolicy,
      quota,
      description,
      gameId,
      imageUrl,
    }
  );
  return response.data;
}

export async function joinGroup(groupId: string) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/join`,
    null,
    { params: { id: groupId } }
  );
  return response.data;
}

export async function leaveGroup(groupId: string) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/leave`,
    null,
    { params: { id: groupId } }
  );
  return response.data;
}

export async function banUserFromGroup({
  groupId,
  userId,
}: {
  groupId: string;
  userId: string;
}) {
  const response = await axios.put(
    `${
      import.meta.env.VITE_APP_API_URL
    }/group/ban-user?groupId=${groupId}&userId=${userId}`
  );
  return response.data;
}

export async function deleteGroup(groupId: string) {
  const response = await axios.delete(
    `${import.meta.env.VITE_APP_API_URL}/group/delete?identifier=${groupId}`
  );
  return response.data;
}

export async function applyGroup(body:any) {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/group/apply?groupId=${body.groupId}`,
    {
      message: body.message,
    }
  );
  return response.data;
}
