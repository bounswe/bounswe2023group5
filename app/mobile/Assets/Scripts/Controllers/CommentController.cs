public class CommentController
{
    
    // Header parameters:
    // string Authorization
    public class CommentReplyRequest
    {
        public string commentContent;
        public string parentComment;
    }

    public class CommentReplyResponse : Comment { }

    // Query parameters:
    // string id
    // Header parameters:
    // string Authorization
    public class CommentEditRequest
    {
        public string commentContent;
    }

    public class CommentEditResponse : Comment { }

    
    // Header parameters:
    // string Authorization
    public class CommentCreateRequest
    {
        public string commentContent;
        public string post;
    }

    public class CommentCreateResponse : Comment { }

    // CommentDeleteRequest has no body
    // Query parameters:
    // string id
    // Header parameters:
    // string Authorization
    

    public class CommentDeleteResponse : Comment { }
}


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