import axios from "axios";

export const createVote = async (body: {
  voteType: "POST" | "REVIEW";
  typeId: string;
  choice: "UPVOTE" | "DOWNVOTE";
}) => {
  const response = await axios.post(
    import.meta.env.VITE_APP_API_URL + "/vote/create",
    body
  );
  return response.data;
};
