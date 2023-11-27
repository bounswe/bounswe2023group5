using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class CreateVote : MonoBehaviour
{
    [SerializeField] private string voteType;
    [SerializeField] private string typeId;
    [SerializeField] private string choice;


    private void Awake()
    {
        Debug.Log("CreateVote is active");
    }

    public void Init(string voteType, string typeId, string choice)
    {
        string url = AppVariables.HttpServerUrl + "/vote/create";
        var achievementCreateRequest = new CreateVoteRequest();
        achievementCreateRequest.voteType = voteType;
        achievementCreateRequest.typeId = typeId;
        achievementCreateRequest.choice = choice;
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
            Debug.Log("Success to create vote: " + response);
        }
        else
        {
            Debug.Log("Error to create vote: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
