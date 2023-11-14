public class VoteController
{
    
}

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

public class GetAllVoteRequest
{
    public string voteType;
    public string typeId;
    public string choice;
    public string votedBy;
    public bool withDeleted;
}

public enum Choice
{
    UPVOTE,
    DOWNVOTE
}

