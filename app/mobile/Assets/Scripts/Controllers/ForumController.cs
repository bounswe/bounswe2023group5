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
    public string poster;
    public string lastEditedAt;
    public string createdAt;
    public bool isEdited;
    public string[] tags;
    public bool inappropriate;
    public int overallVote;
    public int voteCount;
}