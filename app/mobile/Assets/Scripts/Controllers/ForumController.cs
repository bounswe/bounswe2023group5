using System.Collections.Generic;

public class ForumController
{
    
}

public class GetForumRequest
{
    public string forumId;
}

public class PostListEntry
{
    public string id;
    public string title;
    public string postContent;
    public Poster poster;
    public string lastEditedAt;
    public string createdAt;
    public bool isEdited;
    public string[] tags;
    public bool inappropriate;
    public int overallVote;
    public int voteCount;
}

public class Poster
{

    public string id;
    public string createdAt;
    public bool isDeleted;
    public string username;
    public string password;
    public string email;
    public string role;
    public bool isVerified;
    public bool verified;
}