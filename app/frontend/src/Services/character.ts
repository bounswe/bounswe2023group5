import axios from "axios";

export const createCharacter = async ({
  name,
  type,
  gender,
  race,
  status,
  occupation,
  birthDate,
  voiceActor,
  height,
  age,
  games,
  icon,
  description,
}: {
  name: string;
  type: string;
  gender: string;
  race: string;
  status: string;
  occupation: string;
  birthDate: Date;
  voiceActor: string;
  height: string;
  age: string;
  games: any;
  icon: string;
  description: string;
}) => {
  const response = await axios.post(
    `${import.meta.env.VITE_APP_API_URL}/character/create`,
    {
      name,
      type,
      gender,
      race,
      status,
      occupation,
      birthDate,
      voiceActor,
      height,
      age,
      games,
      icon,
      description,
    }
  );

  return response.data;
};
export async function getCharacterByGame(gameId: string) {
  const response = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/character/get-game-characters`,
    { params: { gameId: gameId } }
  );
  return response.data;
}
