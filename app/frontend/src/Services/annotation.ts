import axios from "axios";

export const createAnnotation = async (annotation: any) => {
  const response = await axios.post(
    import.meta.env.VITE_APP_ANNOTATION_API_URL + "/annotation/create",
    annotation
  );
  return response.data;
};

export const updateAnnotation = async (annotation: any) => {
  const response = await axios.put(
    import.meta.env.VITE_APP_ANNOTATION_API_URL + "/annotation/update",
    annotation
  );
  return response.data;
};

export const deleteAnnotation = async (id: string) => {
  id = id.replace("#", "%23");
  const response = await axios.delete(
    `${import.meta.env.VITE_APP_ANNOTATION_API_URL}/annotation/delete?id=${id}`
  );
  return response.data;
};
