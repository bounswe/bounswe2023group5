public class PostController
{
    
}

// Query parameters:
// string id
// Header parameters:
// string Authorization
// Response is PostResponse
public class PostEditRequest
{
    public string title;
    public string postContent;
}

// Header parameters:
// string Authorization
// Response is PostResponse
public class PostCreateRequest
{
    public string title;
    public string postContent;
    public string postImage;
    public string forum;
    public string[] tags;
}

// For GetPostListRequest send no body. Only specify the parameters
// below
// Query parameters:
// bool findDeleted, string[] tags, string search, string forum,
// string sortBy, string sortDirection

public class GetPostListResponse
{
    public string id;
    public string title;
    public string postContent;
    public User poster;
    // public int userVote;
    public string lastEditedAt;
    public string createdAt;
    public bool isEdited;
    public TagResponse[] tags;
    public bool inappropriate;
    public int overallVote;
    public int voteCount;
    public int commentCount;
    
}


// For GetPostDetailRequest do not send a body but specify the
// parameters below
// Query parameters:
// string id
// Header parameters:
// string Authorization
// Response is PostResponse


// For GetPostCommentsRequest send no body, but specify the
// parameters below:
// Query parameters:
// string id
// Header parameters:
// string Authorization
// Response is an array of GetPostCommentsResponse

public class GetPostCommentsResponse
{
    public string id;
    public string commentContent;
    public User commenter;
    public string post;
    public string lastEditedAt;
    public string createdAt;
    public bool isEdited;
    public bool isDeleted;
    public int overallVote;
    public int voteCount;
    public PostComment[] replies;

}

// For PostDeleteRequest, send no body but specify query
// parameters below
// Query parameters:
// string id
// Header parameters:
// string Authorization
// Response is PostResponse

public class PostComment
{
    public string id;
    public string commentContent;
    public User commenter;
    public string post;
    public string lastEditedAt;
    public string createdAt;
    public bool isEdited;
    public int overallVote;
    public int voteCount;
}

public class PostResponse
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string title;
    public string postContent;
    public string postImage;
    public string poster;
    public string forum;
    public string lastEditedAt;
    public string[] tags;
    public bool inappropriate;
    public bool locked;
    public int overallVote;
    public int voteCount;
}