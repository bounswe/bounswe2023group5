public class UserController
{
}

public class UserChangeRoleRequest
{
    public string userRole;
}

public class UserGetAllRequest
{
    public string id;
    public string username;
    public bool isDeleted;
}

// The response is an array of objects [{}], not an object of array of objects {[{}]}.
// Therefore cannot use UserGetAllResponse is deserialization. 
public class UserGetAllResponse
{
    public User[] users;
}

public class UserDeleteRequest
{
    public string id;
}

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
