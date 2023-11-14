import { QueryKey, useMutation, useQueryClient } from "react-query";
import { createVote } from "../../Services/vote";

export function useVote({
  voteType,
  typeId,
  invalidateKey,
}: {
  voteType: "POST" | "REVIEW" | "COMMENT";
  typeId: string;
  invalidateKey: QueryKey;
}) {
  const queryClient = useQueryClient();
  const { mutate: vote, isLoading } = useMutation(
    (choice: "UPVOTE" | "DOWNVOTE") => createVote({ voteType, typeId, choice }),
    {
      onSuccess() {
        queryClient.invalidateQueries(invalidateKey);
      },
    }
  );

  return {
    vote,
    upvote: () => vote("UPVOTE"),
    downvote: () => vote("DOWNVOTE"),
    isLoading,
  };
}
