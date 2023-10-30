using UnityEngine;

public static class PersistenceManager
{
        private static string _userToken = "UserToken";
        private static string _userName = "UserName";
        public static string UserToken
        {
            get => PlayerPrefs.GetString(_userToken, "");
            set => PlayerPrefs.SetString(_userToken, value);
        }
        
        public static string UserName
        {
            get => PlayerPrefs.GetString(_userName, "");
            set => PlayerPrefs.SetString(_userName, value);
        }
}