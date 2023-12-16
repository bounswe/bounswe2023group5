using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class PasswordChangeRequest
{

    public string currentPassword;

    public string newPassword;

    public void SetFields( string password, string newPassword)
    {
        // this.Header.Authorization = token;
        this.currentPassword = password;
        this.newPassword = newPassword;
    }

}