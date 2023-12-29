public class TagController
{
    
}

// Query parameters:
// string id
// Header parameters
// string Authorization (admin token is required)
// Response is TagResponse
public class UpdateTagRequest
{
    public string name;
    public string tagType; 
    public string color;
}

// Header parameters:
// string Authorization (admin token must be given)
// Response is TagResponse
public class CreateTagRequest
{
    public string name;
    public string tagType;
    public string color;
}

// For GetTagRequest send no body but specify the query
// parameters below:
// string id
// Response is TagResponse

// For GetAllTagRequest send no body.
// Query parameters:
// string name, string tagType, string color, bool isDeleted
// Response is an array of TagResponse's

// For DeleteTagRequest send no body.
// Query parameters are:
// string id
// Header parameters are:
// string Authorization (admin token must be given)
// Response is an informative string or simple informative 
// json

public class TagResponse
{
    public string id;
    public string name;
    public string tagType;
    public string color;
    public string createdAt;
    public bool isDeleted;
}

public class TagInfo
{
    public string code;
    public string message;
}