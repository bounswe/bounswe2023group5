public class VoteController
{
    
}

// Send string Authorization as header parameter
// Response is VoteResponse
public class CreateVoteRequest
{
    public string voteType;
    public string typeId;
    public string choice;
}

public class VoteResponse
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string voteType;
    public string typeId;
    public string choice;
    public string votedBy;
}

public class GetVoteRequest
{
    public string id;
}

public class GetAllVotesRequest
{
    public string voteType;
    public string typeId;
    public string choice;
    public string votedBy;
    public bool withDeleted;
}



// For GetVoteRequest, send no body but specify the parameters 
// below
// Query parameters:
// string id
// Response is VoteResponse

// For GetAllVoteRequest send no body, but specify the parameters
// below
// Query parameters:
// string voteType, string typeId, string choice, string votedBy,
// bool withDeleted;
// Response is an array of VoteResponse's (We can use VoteResponse like List<VoteResponse> voteList = JsonConvert....)

// Choice is a string, therefore we are better off not using 
// enum
/*
public enum Choice
{
    UPVOTE,
    DOWNVOTE
}
*/


