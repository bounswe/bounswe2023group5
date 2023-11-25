using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class GetGameAchievements : MonoBehaviour
{
    private string postID;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    private void Start()
    {
        queryParams.Add("gameId ", "1");
        Init();
    }

    public void Init()
    { 
        string url = AppVariables.HttpServerUrl + "/achievement/get-game-achievements" +                      
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
            Debug.Log("Success to get game achievements: " + response);
        }
        else
        {
            Debug.Log("Error to get game achievements: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}
