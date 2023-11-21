using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class AddGroupTag : MonoBehaviour
{
    [SerializeField] private string groupId;
    [SerializeField] private string TagId;

    private void Start()
    {
        Init();

    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/group/add-tag";
        var groupAddTagRequest = new GroupAddTagRequest();
        groupAddTagRequest.groupId = groupId;
        groupAddTagRequest.tagId = TagId;
        string bodyJsonString = JsonUtility.ToJson(groupAddTagRequest);
        StartCoroutine(POST(url, bodyJsonString));
    }
    
    IEnumerator POST(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to add group tag: " + response);
        }
        else
        {
            Debug.Log("Error to add group tag: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
