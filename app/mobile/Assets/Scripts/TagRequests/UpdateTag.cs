using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;
using UnityEditor.UIElements;

public class UpdateTag : MonoBehaviour
{
    [SerializeField] private string id;
    [SerializeField] private string name;
    [SerializeField] private string tagType;
    [SerializeField] private string color;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/tag/update" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}
                     , new []{id});
        var updateTagRequest = new UpdateTagRequest();
        updateTagRequest.name = name;
        updateTagRequest.tagType = tagType;
        updateTagRequest.color = color;
        string bodyJsonString = JsonUtility.ToJson(updateTagRequest);
        StartCoroutine(Put(url, bodyJsonString));
    }
    IEnumerator Put(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "PUT");
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
            var _UpdateTagResponseData = JsonConvert.DeserializeObject<TagResponse>(response);

            // Do things with _UpdateTagResponseData 
            
            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}