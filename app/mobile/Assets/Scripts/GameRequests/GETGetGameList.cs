using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class GETGetGameList : MonoBehaviour
{
    // We can get these variables in another way, depending on the
    // implementation of the program
    [SerializeField] private bool findDeleted;
    [SerializeField] private string gameName;
    [SerializeField] private string[] playerTypes;
    [SerializeField] private string[] genre;
    [SerializeField] private string production;
    [SerializeField] private string[] platform;
    [SerializeField] private string[] artStyle;
    [SerializeField] private string search;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        // Specify the name and the values of parameters you want added to the query in
        // ListToQueryParams function
        string url = AppVariables.HttpServerUrl + "/game/game-by-name" + 
                     ListToQueryParameters.ListToQueryParams(new []{"findDeleted"},
                         new []{findDeleted.ToString()});
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
            var _GETGetGameListResponseData = JsonConvert.DeserializeObject<GameListEntry[]>(response);

            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}