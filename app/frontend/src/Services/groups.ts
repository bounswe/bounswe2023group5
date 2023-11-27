import axios from "axios";

export async function getGroups({
  tags,
  title,
  gameName,
  membershipPolicy,
  sortBy = "CREATION_DATE",
  sortDir = "DESCENDING"
}: {
  tags?: string[];
  title?: string;
  gameName?: string;
  membershipPolicy?: string,
  sortBy?: string;
  sortDir?: string;
} ) {
  const response = await axios.get(
    import.meta.env.VITE_APP_API_URL + "/group/get-all",
    {
      params: {
        title,
        gameName,
        tags,
        membershipPolicy,
        sortBy,
        sortDir,
        
      },
    }
  );
  return response.data;
}

