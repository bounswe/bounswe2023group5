public class ProfileController
{
    
}

public class ProfileRemoveGameRequest
{
    public string game;
}

public class EditProfileRequest
{
    public string username;
    public bool isPrivate;
    public string profilePhoto;
    public string steamProfile;
    public string epicGamesProfile;
    public string xboxProfile;
}

public class ProfileAddGameRequest
{
    public string game;
}



public class EditProfileResponse
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string userId;
    public string[] achievements;
    public float reviewCount;
    public int voteCount;
    public int commentCount;
    public int postCount;
    public bool isReviewedYet;
    public bool isVotedYet;
    public bool isCommentedYet;
    public bool isPostedYet;
    public bool isPrivate;
    public string profilePhoto;
    public string[] games;
    public string steamProfile;
    public string epicGamesProfile;
    public string xboxProfile;
}


public class GetProfileResponse
{
    public string id;
    public User user;
    public AchievementResponse[] achievements;
    public float reviewCount;
    public float voteCount;
    public float commentCount;
    public float postCount;
    public bool isReviewedYet;
    public bool isVotedYet;
    public bool isCommentedYet;
    public bool isPostedYet;
    public bool isPrivate;
    public string profilePhoto;
    public GameDetail[] games;
    public ProfileReview[] reviews;
    public ProfileGroup[] groups;
    public string steamProfile;
    public string epicGamesProfile;
    public string xboxProfile;
}


public class ProfileReview
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string reviewDescription;
    public float rating;
    public string gameId;
    public string reviewedBy;
    public float overallVote;
    public float voteCount;
    public float reportNum;
}

public class ProfileGroup{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string title;
    public string description;
    public string membershipPolicy;
    public string[]  tags;
    public string gameId;
    public string forumId;
    public float quota;
    public string[]  moderators;
    public string[] members;
    public string[] bannedMembers;
    public bool avatarOnly;
}

public class ProfileActivity
{
    public string typeId;
    public string type;
    public string parentId;
    public string parentType;
    public string description;
    public string createdAt;
}


