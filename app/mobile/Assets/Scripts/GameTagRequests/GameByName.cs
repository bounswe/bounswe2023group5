using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class GameByName : MonoBehaviour
{
    // name (of the game) can also be taken from the environment, depending
    // on the implementation of this script
    [SerializeField] private string name;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/game/game-by-name" + 
                     ListToQueryParameters.ListToQueryParams(new []{"name"},
                         new []{name});
        StartCoroutine(Get(url));
    }
    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");

        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode != null && request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            var _GameByNameResponseData = JsonConvert.DeserializeObject<GameDetail>(response);

            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}