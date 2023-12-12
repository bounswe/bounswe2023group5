using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class ForumPostDetail : MonoBehaviour
{
    [SerializeField] private string title;
    [SerializeField] private string postContent;
    private string postID;

    private void Start()
    {
        Init(new[] {"id"},new[] {"b73d132b-a3e3-4776-bbe6-01c2ce0d2d2c"});
    }

    public void Init(string[] pars, string[] vals)
    {
        postID = ListToQueryParameters.GetValueOfParam(
            pars, vals, "id" );
        
        if (postID == "")
        {
            Debug.Log("Id must be specified");
        }
        
        string url = AppVariables.HttpServerUrl + "/post/get-post-detail" +                      
                     ListToQueryParameters.ListToQueryParams(pars, vals);
        StartCoroutine(Get(url));
    }
    
    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get forum post detail: " + response);
        }
        else
        {
            Debug.Log("Error to get forum post detail: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}