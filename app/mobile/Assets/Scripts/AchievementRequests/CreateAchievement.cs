using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class CreateAchievement : MonoBehaviour
{
    [SerializeField] private string title;
    [SerializeField] private string description;
    [SerializeField] private string icon;
    [SerializeField] private string type;
    [SerializeField] private string game;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/achievement/create";
        var achievementCreateRequest = new AchievementCreateRequest();
        achievementCreateRequest.title = title;
        achievementCreateRequest.description = description;
        achievementCreateRequest.icon = icon;
        achievementCreateRequest.type = type;
        achievementCreateRequest.game = game;
        string bodyJsonString = JsonUtility.ToJson(achievementCreateRequest);
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
            Debug.Log("Success to create achievement: " + response);
        }
        else
        {
            Debug.Log("Error to create achievement: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
