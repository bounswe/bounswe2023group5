import { QueryKey, useMutation, useQueryClient } from "react-query";
import { createVote } from "../../Services/vote";

export function useVote({
  voteType,
  typeId,
  invalidateKey,
  invalidateKeys,
}: {
  voteType: "POST" | "REVIEW" | "COMMENT";
  typeId: string;
  invalidateKey?: QueryKey;
  invalidateKeys?: QueryKey[];
}) {
  const queryClient = useQueryClient();
  const { mutate: vote, isLoading } = useMutation(
    (choice: "UPVOTE" | "DOWNVOTE") => createVote({ voteType, typeId, choice }),
    {
      onSuccess() {
        queryClient.invalidateQueries(["home"]);
        if (invalidateKey) {
          queryClient.invalidateQueries(invalidateKey);
        }
        if (invalidateKeys) {
          invalidateKeys.forEach((key) => queryClient.invalidateQueries(key));
        }
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
