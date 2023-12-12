using System.Collections.Generic;

public class GameController
{
}

// Below is for the POST endpoint for game/get-game-list
// Response is a list of GameListEntry's
public class GetGameListRequest
{
    public bool findDeleted;
    public string gameName;
    public string[] playerTypes;
    public string[] genre;
    public string production;
    public string[] platform;
    public string[] artStyle;
    public string search;
}

// Below is for the GET endpoint for game/get-game-list
// Used in GETGetGameList.cs
// Response is a list of GameListEntry's
// Query parameters:
// bool findDeleted, string gameName, string[] playerTypes
// string[] genre, string production, string[] platform
// string[] artStyle, string search

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

// Instead of GetGameResponse, GameDetail's are being returned
/*
public class GetGameResponse
{
    public GameDetail game;
}
*/
public class GameDetail
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string gameName;
    public string gameDescription;
    public string gameIcon;
    public double overallRating;
    public double ratingCount;
    public string releaseDate;
    public string forum;
    public TagResponse[] playerTypes;
    public TagResponse[] genre;
    public TagResponse production;
    public TagResponse duration;
    public TagResponse[] platforms;
    public TagResponse[] artStyles;
    public TagResponse developer;
    public TagResponse[] otherTags;
    public string minSystemReq;
    // public TagResponse[] allTags;
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

// For GameByNameRequest send name of the game as a query parameter
// Response is a GetGameResponse

