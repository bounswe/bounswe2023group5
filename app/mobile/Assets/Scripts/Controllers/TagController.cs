public class TagController
{
    
}

// Send id as query parameter
// Response is TagResponse
public class UpdateTagRequest
{
    public string name;
    // PlayerType may be problematic, a string would be better
    public string tagType; 
    public string color;
}

// Response is TagResponse
public class CreateTagRequest
{
    public string name;
    public string tagType;
    public string color;
}

// For GetTagRequest send no body but specify the id as 
// a query parameter
// Response is TagResponse

// For GetAllTagRequest send no body. Query parameters are:
// string name, string tagType, string color, bool isDeleted

// For DeleteTagRequest send no body, query parameters are:
// string id

public class TagResponse
{
    public string id;
    public string name;
    public string tagType;
    public string color;
    public string createdAt;
    public bool isDeleted;
}

// In requests and responses PlayerType's are given in string
// enum would lead to ints, therefore we shouldn't use enum
/*
public enum PlayerType
{
    PLAYER_TYPE, 
    GENRE, 
    PRODUCTION, 
    DURATION, 
    PLATFORM, 
    ART_STYLE, 
    DEVELOPER, 
    MONETIZATION, 
    POST, 
    OTHER 
}
*/