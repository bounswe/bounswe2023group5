import { useQuery } from "react-query";
import CharacterDetails from "../../Components/Character/CharacterDetails";
import { getCharacterByGame } from "../../Services/character";

function Char() {
  const gameId = "841cbf45-90cc-47b7-a763-fa3a18218bf9";
  const { data: characters } = useQuery(["char", gameId], () =>
    getCharacterByGame(gameId)
  );

  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      {characters?.map((character: any) => (
        <CharacterDetails character={character}></CharacterDetails>
      ))}
    </div>
  );
}

export default Char;
