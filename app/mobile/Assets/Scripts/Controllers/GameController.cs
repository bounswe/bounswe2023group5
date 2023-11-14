using System.Collections.Generic;

public class GameController
{
}

// Response is a list of GameListEntry's
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

// Also send Authorization as query parameter
// Response is GameDetail
public class CreateGameRequest
{
    public string gameName;
    public string gameDescription;
    public string gameIcon;
    public string releaseDate;
    public string[] playerTypes;
    public string[] genre;
    public string production;
    public string[] platforms;
    public string[] artStyles;
    public string developer;
    public string[] otherTags;
    public string minSystemReq;
}

public class GameAddTagRequest
{
    public string gameId;
    public string tagId;
}

public class GameAddTagResponse
{
    public string gameId;
    public TagResponse addedTag;
}

// For GetGame request, send no body but only gameId
// as a query parameter


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
    public string overallRating;
    public string ratingCount;
    public string releaseDate;
    public string forum;
    public string[] playerTypes;
    public string[] genre;
    public string production;
    public string duration;
    public string[] platforms;
    public string[] artStyles;
    public string developer;
    public string[] otherTags;
    public string minSystemReq;
}

// For GetAllTagsRequest send gameId as a query parameter


public class GetAllTagsResponse
{
    public string[] playerTypes;
    public string[]  genre;
    public string production;
    public string duration;
    public string[] platforms;
    public string[] artStyles;
    public string developer;
    public string[] otherTags;
}



