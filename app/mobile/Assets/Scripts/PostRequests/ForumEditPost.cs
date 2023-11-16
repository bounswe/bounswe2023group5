using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class ForumEditPost : MonoBehaviour
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
        
        string url = AppVariables.HttpServerUrl + "/post/edit" + 
                     ListToQueryParameters.ListToQueryParams(pars, vals);
        var postEditRequest = new PostEditRequest();
        postEditRequest.title = title;
        postEditRequest.postContent = postContent;
        string bodyJsonString = JsonUtility.ToJson(postEditRequest);
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
            Debug.Log("Success to edit forum post: " + response);
        }
        else
        {
            Debug.Log("Error to edit forum post: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}

