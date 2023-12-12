public class AuthController
{
    
}

public class VerifyResetCodeRequest
{
    public string userEmail;
    public string resetCode;
}

// Actually this response is not an object but a single 
// string
public class VerifyResetCodeResponse
{
    public string token;
}

public class RegisterRequest
{
    public string username;
    public string email;
    public string password;
}


public class RegisterResponse
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

// MeRequest is just sending a Post without a body. 
// The request should have a Authorization header
// parameter


// MeResponse is same as UserData

public class LoginRequest
{
    public string email;
    public string password;
}

public class LoginResponse
{
    public UserData user;
    public string token;
}

public class ForgetPasswordRequest
{
    public string email;
}

// Also Authorization must be sent in headers when
// making a request
public class ChangePasswordRequest
{
    public string currentPassword;
    public string newPassword;
}

// Also Authorization must be sent in headers when
// making a request
public class ChangeForgetPasswordRequest
{
    public string newPassword;
}

public class UserData
{
    public string username;
    public string email;
    public string id;
    public string role;
    public bool isVerified;
}
