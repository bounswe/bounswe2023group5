public class UserController
{
    
}

// With the body send the parameters below:
// Query parameters:
// string id
public class UserChangeRoleRequest
{
    public string userRole;
}

// For UserGetAllRequest, do not sent any body but send the 
// parameters below:
// Query parameters:
// string id, string username, bool isDeleted;
// The response is an array of User's

// For UserDeleteRequest, do not sent any body but send the 
// parameters below:
// Query parameters:
// string id


public class User
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
