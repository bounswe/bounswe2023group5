using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using UnityEngine;
using TMPro;

public  class ChangePasswordHelper
{
    private static List<string> _userEmails;
    private static UserData _userData;
	public static TMP_Text errorText;

    static ChangePasswordHelper()
    {
        string userJson = Resources.Load<TextAsset>("SignUp").text;
        _userData = JsonConvert.DeserializeObject<UserData>(userJson);

	}

    public static (bool, string) ChangeUserPassword(PasswordChangeRequest pcr)
    {
        _userEmails = _userData.users.ConvertAll(x => x.email);
        
        // If the user email is null or it is non-existant
        if(pcr.email == null || ! _userEmails.Contains(pcr.email))
        {
			var message = "There is no such email address";
			return (false, message);
        }

        UserInfo user = _userData.users.FirstOrDefault(user => user.email == pcr.email);

        // If passwords does not match 
        if (user.password != pcr.password)
        {
			var message = "Wrong password";
            return (false, message);
        }
        
        // Else change the password
        user.password = pcr.newPassword;

        // Serialize the new object
        string updatedJson = JsonConvert.SerializeObject(_userData);
        
        // Write it back
        System.IO.File.WriteAllText(Application.dataPath + "/Resources/SignUp.json", updatedJson);
        return (true, "");
    }
}