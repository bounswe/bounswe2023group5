using UnityEngine;

public static class PersistenceManager
{
        private static string _userToken = "UserToken";
        private static string _userName = "UserName";
        private static string _Id = "Id";
        private static string _password = "Password";
        private static string _role = "Role";
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
        
        public static string id
        {
            get => PlayerPrefs.GetString(_Id, "");
            set => PlayerPrefs.SetString(_Id, value);
        }
        
        public static string Password
        {
            get => PlayerPrefs.GetString(_password, "");
            set => PlayerPrefs.SetString(_password, value);
        }
        
        public static string Role
        {
            get => PlayerPrefs.GetString(_role, "");
            set => PlayerPrefs.SetString(_role, value);
        }
}