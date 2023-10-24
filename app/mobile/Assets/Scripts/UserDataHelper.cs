using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

public static class UserDataHelper
{
    private static List<string> _userEmails;
    private static UserData _userData;
    
    static UserDataHelper()
    {
        string userJson = Resources.Load<TextAsset>("SignUp").text;
        _userData = JsonConvert.DeserializeObject<UserData>(userJson);
        
    }

    public static bool WriteNewUserData(UserInfo user)
    {
        _userEmails = _userData.users.ConvertAll(x => x.email);
        if(user.email == null || _userEmails.Contains(user.email))
        {
            Debug.Log("Email already exists");
            return false;
        }
        _userData.users.Add(user);
        string userJson = JsonConvert.SerializeObject(_userData);
        System.IO.File.WriteAllText(Application.dataPath + "/Resources/SignUp.json", userJson);
        return true;
    }
    public static bool DoesEmailExist(string email)
    {
        _userEmails = _userData.users.ConvertAll(x => x.email);
        return _userEmails.Contains(email);
    }
}