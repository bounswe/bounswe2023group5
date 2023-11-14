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
    findDeleted = false,
    sortBy = "CREATION_DATE",
    postId,
    sortDirection = "DESCENDING",
  }: {
    findDeleted?: boolean;
    postId: string;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    const id:string = postId;
    const response = await axios.get(
      import.meta.env.VITE_APP_API_URL + "/post/get-post-comments",
      {
        params: {
          findDeleted,
          sortBy,
          id,
          sortDirection,
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