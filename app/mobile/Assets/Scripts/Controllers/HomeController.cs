using System;
using System.Collections.Generic;

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
    public User poster;
    public AchievementResponse achievements;
    public Character character;
    public string userVote;
    public string forum;
    public string type;
    public string typeId;
    public string typeName;
    public DateTime lastEditedAt;
    public TagResponse[] tags;
    public bool inappropriate;
    public bool locked;
    public int overallVote;
    public int voteCount;
    public DateTime createdAt;
    public bool isDeleted;
    public bool isPromoted;
    
}

public class Character
{
    public string id;
    public DateTime createdAt;
    public bool isDeleted;
    public string name;
    public string icon;
    public string description;
    public string[] games;
    public string type;
    public string gender;
    public string race;
    public string status;
    public string occupation;
    public string birthDate;
    public string voiceActor;
    public string height;
    public string age;
    public Dictionary<string,string> customFields;
    
    
}
