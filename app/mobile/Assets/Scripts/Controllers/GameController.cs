using System.Collections.Generic;

public class GameController
{
}

public class GetGameListRequest
{
    public bool findDeleted;
    public string[] playerTypes;
    public string[] genre;
    public string production;
    public string[] platform;
    public string[] artStyle;
    public string search;
}

public class GameListEntry
{
    public string id;
    public string gameName;
    public string gameDescription;
    public string gameIcon;
}

public class GetGameRequest
{
    public string gameId;
}

public class GetGameResponse
{
    public GameDetail game;
}

public class GameDetail
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string gameName;
    public string gameDescription;
    public string gameIcon;
    public string releaseDate;
    public List<AddedTag> playerTypes;
    public List<AddedTag> genre;
    public AddedTag production;
    public AddedTag duration;
    public List<AddedTag> platforms;
    public List<AddedTag> artStyles;
    public AddedTag developer;
    public List<AddedTag> otherTags;
    public string minSystemReq;
}

public class GetAllTagsRequest
{
    public string gameId;
}

public class GetAllTagsResponse
{
    public List<AddedTag> playerTypes;
    public List<AddedTag> genre;
    public AddedTag production;
    public AddedTag duration;
    public List<AddedTag> platforms;
    public List<AddedTag> artStyles;
    public AddedTag developer;
    public List<AddedTag> otherTags;
}

public class GameAddTagRequest
{
    public string gameId;
    public string tagId;
}

public class GameAddTagResponse
{
    public string gameId;
    public AddedTag addedTag;
}

public class AddedTag
{
    public string id;
    public string createdAt;
    public string isDeleted;
    public string name;
    public PlayerType tagType;
    public string color;
}
