using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class EditProfile : MonoBehaviour
{
    [SerializeField] private TMP_InputField username;
    [SerializeField] private bool isPrivate;
    [SerializeField] private string profilePhoto = "";
    [SerializeField] private Toggle privateToggle;
    [SerializeField] private Toggle publicToggle;
    [SerializeField] private TMP_InputField steamProfile;
    [SerializeField] private TMP_InputField epicGamesProfile;
    [SerializeField] private TMP_InputField xboxProfile;
    private string id;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    private void OnEnable()
    {
        username.text = PersistenceManager.UserName;
    }

    public void SetProfilePrivate()
    {
        if (!privateToggle.isOn)
        {
            return;
        }
        publicToggle.isOn = false;
        isPrivate = true;
        Debug.Log(isPrivate);
    }
    public void SetProfilePublic()
    {
        if (!publicToggle.isOn)
        {
            return;
        }
        privateToggle.isOn = false;
        isPrivate = false;
        Debug.Log(isPrivate);
    }

    public void Init()
    {
        queryParams.Add("id", PersistenceManager.ProfileId);
        string url = AppVariables.HttpServerUrl + "/profile/edit" +
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
        var editProfileRequest = new EditProfileRequest();
        editProfileRequest.username = username.text;
        editProfileRequest.isPrivate = isPrivate;
        editProfileRequest.profilePhoto = profilePhoto;
        editProfileRequest.steamProfile = steamProfile.text;
        editProfileRequest.epicGamesProfile = epicGamesProfile.text;
        editProfileRequest.xboxProfile = xboxProfile.text;
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
            PersistenceManager.UserName = username.text;
        }
        else
        {
            Debug.Log("Error to edit profile: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
