import axios from "axios";

export const createComment = async ({
  post,
  commentContent,
}: {
  post: string;
  commentContent: string;
}) => {
    console.log("post", post);
  const response = await axios.post(
    import.meta.env.VITE_APP_API_URL + "/comment/create",
    {
        post,
        commentContent,
    }
  );
  return response.data;
};


