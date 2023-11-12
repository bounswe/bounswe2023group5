public class ReviewController
{
    
}

public class ReviewUpdateRequest
{
    public string id;
    public string reviewDescription;
    public string rating;
}

public class ReviewCreateRequest
{
    public string reviewDescription;
    public string rating;
    public string gameId;
}

public class ReviewCreateResponse
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string reviewDescription;
    public string rating;
    public string gameId;
    public string reviewedBy;
    public string overallVote;
    public string voteCount;
    public string reportNum;
}

public class ReviewGetRequest
{
    public string id;
}

public class ReviewGetResponse
{
    public string id;
    public string reviewDescription;
    public string rating;
    public string gameId;
    public string reviewedBy;
    public string overallVote;
    public string reportNum;    
    public string createdAt;
}

public class ReviewGetAllRequest
{
    public string gameId;
    public string reviewedBy;
    public bool withDeleted;
}

public class ReviewGetAllResponse
{
    public string id;
    public string reviewDescription;
    public string rating;
    public string gameId;
    public string reviewedUser;
    public string overallVote;
    public string reportNum;    
    public string createdAt;
}

public class ReviewDeleteRequest
{
    public string id;
}


