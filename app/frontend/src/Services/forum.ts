import axios from "axios";
export const getPostList = async ({
  findDeleted = false,
  tags = [],
  search = "",
  sortBy = "CREATION_DATE",
  forum,
  sortDirection = "DESCENDING",
}: {
  findDeleted?: boolean;
  tags?: string[];
  search?: string;
  forum: string;
  sortBy?: string;
  sortDirection?: string;
}) => {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/post/get-post-list",
    {
      params: {
        findDeleted,
        tags,
        search,
        sortBy,
        forum,
        sortDirection,
      },
    }
  );
  return response.data;
};

export const createPost = async ({
  title,
  postContent,
  postImage,
  forum,
  tags = [],
}: {
  title: string;
  postContent: string;
  postImage?: string;
  forum: string;
  tags?: string[];
}) => {
  const response = await axios.post(
    import.meta.env.VITE_APP_API_URL + "/post/create",
    {
      title,
      postContent,
      postImage,
      forum,
      tags,
    }
  );
  return response.data;
};

export const editPost = async ({
  title,
  postContent,
  postImage,
  id,
  tags = [],
}: {
  title: string;
  postContent: string;
  postImage?: string;
  id: string;
  tags?: string[];
}) => {
  const response = await axios.post(
    import.meta.env.VITE_APP_API_URL + "/post/edit?id=" + id,
    {
      title,
      postContent,
      postImage,
      tags,
    }
  );
  return response.data;
};

export const getPost = async (id: string) => {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/post/get-post-detail",
    {
      params: {
        id,
      },
    }
  );
  return response.data;
};

export const deletePost = async (id: string) => {
  const response = await axios.delete(
    `${import.meta.env.VITE_APP_API_URL}/post/delete?id=${id}`
  );
  return response.data;
};
