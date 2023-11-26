import axios from "axios";
export const uploadImage = async (image: any, folder: string) => {
  const formData = new FormData();
  formData.append("image", image);
  const config = {
    headers: {
      "content-type":
        "multipart/form-data; boundary=----WebKitFormBoundaryqTqJIxvkWFYqvP5s",
    },
  };

  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/image/upload?folder=${folder}`,
    formData,
    config
  );

  return response.data;
};
