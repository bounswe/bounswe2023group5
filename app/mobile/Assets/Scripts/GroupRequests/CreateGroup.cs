using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class CreateGroup : MonoBehaviour
{
    private string groupId;

    private void Start()
    {
        Init();

    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/group/create";
        StartCoroutine(POST(url));
    }
    
    IEnumerator POST(string url)
    {
        var request = new UnityWebRequest(url, "POST");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to create group: " + response);
        }
        else
        {
            Debug.Log("Error to create group: " + response);
        }
        request.downloadHandler.Dispose();
    }
}
