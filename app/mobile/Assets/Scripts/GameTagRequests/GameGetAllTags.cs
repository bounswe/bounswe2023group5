using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class GameGetAllTags : MonoBehaviour
{
    // gameId can also be taken from the environment, depending on the
    // implementation of this script
    [SerializeField] private string gameId;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/game/get-all-tags" + 
                     ListToQueryParameters.ListToQueryParams(new []{"gameId"},
                         new []{gameId});
        StartCoroutine(Post(url));
    }
    IEnumerator Post(string url)
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
            var _GetAllTagsResponseData = JsonConvert.DeserializeObject<GetAllTagsResponse>(response);

            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}