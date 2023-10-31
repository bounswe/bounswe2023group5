public class AuthController
{
    
}

public class VerifyResetCodeRequest
{
    public string userEmail;
    public string resetCode;
}

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
    public string username;
    public string email;
    public string password;
    public string role;
    public bool verified;
    public bool isDeleted;
    public string createdAt;
}

public class MeRequest
{
    public string Authorization;
}

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

public class ChangePasswordRequest
{
    public string Authorization;
    public string currentPassword;
    public string newPassword;
}

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

public class DeleteAccountRequest
{
    public string id;
}

public class DeleteAccountResponse
{
    public bool IsDeleted;
}
