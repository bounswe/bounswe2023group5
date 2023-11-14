public class TagController
{
    
}

public class UpdateTagRequest
{
    public string id;
    public string name;
    public PlayerType tagType;
    public string color;
}

public class CreateTagRequest
{
    public string name;
    public PlayerType tagType;
    public string color;
}

public class GetTagRequest
{
    public string id;
}

public class GetAllTagRequest
{
    public string name;
    public PlayerType tagType;
    public string color;
    public bool isDeleted;
}

public class DeleteTagRequest
{
    public string id;
}

public class TagResponse
{
    public string id;
    public string name;
    public PlayerType tagType;
    public string color;
    public string createdAt;
    public bool isDeleted;
}

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