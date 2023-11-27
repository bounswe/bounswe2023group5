using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class EditProfile : MonoBehaviour
{
    [SerializeField] private string username;
    [SerializeField] private bool isPrivate;
    [SerializeField] private string profilePhoto;
    [SerializeField] private string steamProfile;
    [SerializeField] private string epicGamesProfile;
    [SerializeField] private string xboxProfile;
    private string id;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    private void Start()
    {
        queryParams.Add("id ", "1");
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/profile/edit" +
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
        var editProfileRequest = new EditProfileRequest();
        editProfileRequest.username = username;
        editProfileRequest.isPrivate = isPrivate;
        editProfileRequest.profilePhoto = profilePhoto;
        editProfileRequest.steamProfile = steamProfile;
        editProfileRequest.epicGamesProfile = epicGamesProfile;
        editProfileRequest.xboxProfile = xboxProfile;
        string bodyJsonString = JsonUtility.ToJson(editProfileRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to edit profile: " + response);
        }
        else
        {
            Debug.Log("Error to edit profile: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
