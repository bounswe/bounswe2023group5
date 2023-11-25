import axios from "axios";

export const getAchievementByGame = async ({ gameId }: { gameId: string }) => {
    const response = await axios.get(
      import.meta.env.VITE_APP_API_URL + "/achievement/get-game-achievements",
      {
        params: {
          gameId,
        },
      }
    );
    return response.data;
  };