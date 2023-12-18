using System;

public class HomeController
{
        
}

public class HomeRequest
{
    public string sortBy;
    public string sortDirection;
}

public class HomeResponse
{
    public string id;
    public string title;
    public string postContent;
    public string postImage;
    public string poster;
    public string forum;
    public string type;
    public string typeId;
    public string typeName;
    public DateTime lastEditedAt;
    public string[] tags;
    public bool inappropriate;
    public bool locked;
    public int overallVote;
    public int voteCount;
    public DateTime createdAt;
    public bool isDeleted;
}