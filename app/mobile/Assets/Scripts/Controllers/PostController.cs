public class PostController
{
    
}

public class PostEditRequest
{
    public string id;
    public string title;
    public string postContent;
}

public class PostCreateRequest
{
    public string title;
    public string postContent;
    public string postImage;
    public string forum;
    public string[] tags;
}

public class GetPostListRequest
{
    public bool findDeleted;
    public string[] tags;
    public string search;
    public string forum;
    public string sortBy;
    public string sortDirection;
}

public class GetPostListResponse
{
    public string id;
    public string title;
    public string postContent;
    public User poster;
    public string lastEditedAt;
    public string createdAt;
    public bool isEdited;
    public string[] tags;
    public bool inappropriate;
    public int overallVote;
    public int voteCount;
    public int commentCount;
    
}

public class GetPostDetailRequest
{
    public string id;
}

public class GetPostCommentsRequest
{
    public string id;
}

public class GetPostCommentsResponse
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
    public PostComment[] replies;

}

public class PostDeleteRequest
{
    public string id;
}

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