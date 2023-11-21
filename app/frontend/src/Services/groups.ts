import axios from "axios";


export async function getGroups (
    title ?: string,
    gameName ?: string,
    tags = [],
    membershipPolicy ?: string,
    sortBy = "CREATION_DATE",
    sortDirection = "DESCENDING",
  ) {
    const response = await axios.get(
      import.meta.env.VITE_APP_API_URL + "/group/get-all",
      {
        params: {
          title,
          gameName,
          tags,
          membershipPolicy,
          sortBy,
          sortDirection,
        },
      }
    );
    return response.data;
  };