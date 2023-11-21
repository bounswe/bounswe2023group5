using System.Collections;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class UpdateGroup : MonoBehaviour
{
    [SerializeField] private string title;
    [SerializeField] private string description;
    [SerializeField] private string membershipPolicy;
    [SerializeField] private int quota;
    [SerializeField] private bool avatarOnly;
    
    private string groupID;

    private void Start()
    {
        Init("");
    }

    public void Init(string _groupID)
    {
        groupID = _groupID;
        string url = AppVariables.HttpServerUrl + "/group/update"+"?id=" + groupID;
        var groupUpdateBody = new GroupUpdateRequest();
        groupUpdateBody.title = title;
        groupUpdateBody.description = description;
        groupUpdateBody.membershipPolicy = membershipPolicy;
        groupUpdateBody.quota = quota;
        groupUpdateBody.avatarOnly = avatarOnly;
        string bodyJsonString = JsonConvert.SerializeObject(groupUpdateBody);
        StartCoroutine(PUT(url, bodyJsonString));
    }

    IEnumerator PUT(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "PUT");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success to update group: " + response);
        }
        else
        {
            Debug.Log("Error to update group: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
