public class CommentController
{
    public class CommentReplyRequest
    {
        public string commentContent;
        public string parentComment;
    }

    public class CommentReplyResponse : Comment { }

    public class CommentEditRequest
    {
        public string id;
        public string commentContent;
    }

    public class CommentEditResponse : Comment { }

    public class CommentCreateRequest
    {
        public string commentContent;
        public string post;
    }

    public class CommentCreateResponse : Comment { }

    public class CommentDeleteRequest
    {
        public string id;
    }

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