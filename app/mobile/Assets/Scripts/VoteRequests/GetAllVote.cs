using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class GetAllVote : MonoBehaviour
{
    private string id;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    private void Start()
    {
        queryParams.Add("voteType", "1");
        queryParams.Add("typeId", "1");
        queryParams.Add("choice", "1");
        queryParams.Add("votedBy", "1");
        queryParams.Add("withDeleted", false.ToString());
        Init();
    }

    public void Init()
    { 
        string url = AppVariables.HttpServerUrl + "/vote/get-all" +                      
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
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
            Debug.Log("Success to get all vote: " + response);
        }
        else
        {
            Debug.Log("Error to get all vote: " + response);
        }
        request.downloadHandler.Dispose();
    }
}
