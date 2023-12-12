public class AchievementController
{
    
}

public class AchievementUpdateRequest
{
    public string title;
    public string description;
    public string icon;
}

public class AchievementGrantRequest
{
    public string userId;
    public string achievementId;
}

public class AchievementCreateRequest
{
    public string title;
    public string description;
    public string icon;
    public string type;
    public string game;
}

public class AchievementResponse
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string title;
    public string description;
    public string icon;
    public string type;
    public string game;
}





