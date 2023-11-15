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

export const getCommentList = async ({
    postId,
  }: {
    postId: string;
  }) => {
    const id:string = postId;
    const response = await axios.get(
      import.meta.env.VITE_APP_API_URL + "/post/get-post-comments",
      {
        params: {
          id,
        },
      }
    );
    return response.data;
  };
  
  export async function deleteComment(id: string) {
    await axios.delete(`${import.meta.env.VITE_APP_API_URL}/comment/delete`, {
         params: {
             id
         }
     });
 }


 export const createReply = async ({
  parentComment,
  commentContent,
}: {
  parentComment: string;
  commentContent: string;
}) => {
  const response = await axios.post(
    import.meta.env.VITE_APP_API_URL + "/comment/reply",
    {
      parentComment,
        commentContent,
    }
  );
  return response.data;
};