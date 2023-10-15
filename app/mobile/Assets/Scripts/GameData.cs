public class GameData
{
        public GameInfo[] games;
}

public class GameInfo
{
        public string name;
        public string year;
        public string info;
        public CharacterInfo[] characters;
}

public class CharacterInfo
{
        public string name;
        public string role;
        public AbilityInfo abilities;
}

public class AbilityInfo
{
        public string ability1;
        public string ability2;
        public string ability3;
        public string ability4;
}