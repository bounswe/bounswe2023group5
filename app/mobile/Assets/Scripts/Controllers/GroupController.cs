public class GroupController
{
}

public class GroupUpdateRequest
{
    public string title;
    public string description;
    public string membershipPolicy;
    public int quota;
    public bool avatarOnly;
}

// For GroupGetAllRequest send no body.
// Query parameters:
// string title, string membershipPolicy, string[] tags, 
// string gameName, string sortBy, string sortDirection
// Response: GroupResponse[]

// For GroupGetRequest send no body.
// Query parameters:
// string id (required)
// Response: GroupResponse

public class GroupResponse{
    public string id;
    public string createdAt;
    public string title;
    public string description;
    public string membershipPolicy;
    public TagResponse[]  tags;
    public string gameId;
    public string forumId;
    public int quota;
    public string[]  moderators;
    public string[] members;
    public string[] bannedMembers;
    public bool avatarOnly;
}

// Header parameters:
// string Authorization (required)
// Response: GroupResponse
public class GroupCreateRequest
{
    public string title;
    public string description;
    public string membershipPolicy;
    public string[] tags;
    public string gameId;
    public int quota;
    public bool avatarOnly;
}


// ! must be tested
// For GroupJoinRequest send no body.
// Query parameters:
// string id (required)
// Header parameters:
// string Authorization (required)
// Response is "true" if successful


// ! must be tested
// For GroupLeaveRequest send no body.
// Query parameters:
// string id (required)
// Header parameters:
// string Authorization (required)
// Response is "true" if successfull


// Header parameters:
// string Authorization (required)
// Response: GroupAddTagResponse
public class GroupAddTagRequest
{
    public string groupId;
    public string tagId;
}

public class GroupAddTagResponse
{
    public string groupId;
    public TagResponse addedTag;
}

// Header parameters:
// string Authorization (required)
// Response: "true" if successful
public class GroupRemoveTagRequest
{
    public string groupId;
    public string tagId;
}