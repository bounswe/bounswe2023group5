using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class GetAllTags : MonoBehaviour
{
    [SerializeField] private string name;
    [SerializeField] private string color;
    [SerializeField] private string tagType;
    [SerializeField] private bool isDeleted;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        // We can add any of the query parameters (name, color, tagType, 
        // isDeleted) in ListToQueryParams. Or we may add no query 
        // parameters.
        string url = AppVariables.HttpServerUrl + "/tag/get-all" +
                     ListToQueryParameters.ListToQueryParams(
                         new[] { "name" }, new[] { name });
        StartCoroutine(Get(url));
    }
    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            var _GetAllTagsResponseData = JsonConvert.DeserializeObject<TagResponse[]>(response);

            // Do things with _GetAllTagsResponseData 
            
            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}