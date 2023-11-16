public class CommentController
{
    
}

// Send Authorization as header parameter
// Response is Comment
public class CommentReplyRequest
{
    public string commentContent;
    public string parentComment;
}


// Send id as query parameter and Authorization as
// header parameter
// Response is Comment
public class CommentEditRequest
{
    public string commentContent;
}

// Send Authorization as header parameter
// Response is Comment
public class CommentCreateRequest
{
    public string commentContent;
    public string post;
}


// For making a comment delete request, send no body,
// only specify id as a query parameter and Authorization
// as a header parameter
// Response is Comment

public class Comment
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string commentContent;
    public string commenter;
    public string post;
    public string parentComment;
    public string lastEditedAt;
    public int overallVote;
    public int voteCount;
}


