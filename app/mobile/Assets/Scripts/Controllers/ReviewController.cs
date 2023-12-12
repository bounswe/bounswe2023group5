public class ReviewController
{
    
}

// Send the parameters below too:
// Query parameters:
// string id
// Header parameters:
// string Authorization
public class ReviewUpdateRequest
{
    public string reviewDescription;
    public string rating;
}

// Send the parameters below too:
// Header parameters:
// string Authorization
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
    public int rating;
    public string gameId;
    public string reviewedBy;
    public int overallVote;
    public int voteCount;
    public int reportNum;
}

// For ReviewGetRequest, send no body, but send the parameters
// below:
// Query parameters:
// string id;
// Response is ReviewResponse



// For ReviewGetAllRequest, send no body but the parameters 
// below:
// Query parameters:
// string gameId, string reviewedBy, bool withDeleted
// Response is an array of ReviewResponse's

public class ReviewResponse
{
    public string id;
    public string reviewDescription;
    public float rating;
    public string gameId;
    public string reviewedUser;
    public int overallVote;
    public int reportNum;    
    public string createdAt;
}

// For ReviewDeleteRequest, send no body but the parameters 
// below:
// Query parameters:
// string id
// Header parameters:
// string Authorization


