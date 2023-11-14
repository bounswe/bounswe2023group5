import axios from "axios";
export const getTags = async () => {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/tag/get-all`
  );
  return response.data;
};

export const getTagById = async (id: string) => {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/tag/get?id=${id}`
  );
  return response.data;
};

export const addTag = async (data: {
  name: string;
  tagType: string;
  color: string;
}) => {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/tag/create`,
    data
  );
  return response.data;
};

export const updateTag = async ({
  id,
  name,
  tagType,
  color,
}: {
  id: string;
  name: string;
  tagType: string;
  color: string;
}) => {
  const response = await axios.put(
    `${import.meta.env.VITE_APP_API_URL}/tag/update?id=${id}`,
    { name, tagType, color }
  );
  return response.data;
};

export const deleteTag = async (id: string) => {
  const response = await axios.delete(
    `${import.meta.env.VITE_APP_API_URL}/tag/delete?id=${id}`
  );
  return response.data;
};
