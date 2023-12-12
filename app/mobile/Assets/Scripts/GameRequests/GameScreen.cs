using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GameScreen : MonoBehaviour
{

    private CanvasManager canvasManager;
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform gamePageParent;
    private List<GamePage> gamePages = new List<GamePage>();

    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void Start()
    {
        ListGames(null);
    }


    private void ListGames(GetGameListRequest gameRequestData)
    {
        string url = AppVariables.HttpServerUrl + "/game/get-game-list";

        
        string bodyJsonString = (gameRequestData == null) ? "" :
            JsonConvert.SerializeObject(gameRequestData);

        /*
        var gameRequestData = new GetGameListRequest();
        //todo: add filters
        string bodyJsonString = JsonConvert.SerializeObject(gameRequestData);
        */
        
        StartCoroutine(Post(url, bodyJsonString));
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
        foreach (var gamePage in gamePages)
        {
            Destroy(gamePage.gameObject);
        }
        gamePages.Clear();
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log(response);
        var _gamesData = JsonConvert.DeserializeObject<GameListEntry[]>(response);
        if (request.responseCode != 200 || _gamesData == null)
        {
            Debug.Log("error");
        }
        else
        {
            foreach (var gameData in _gamesData)
            {
                GamePage newGamePage = Instantiate(Resources.Load<GamePage>("Prefabs/GamePage"), gamePageParent);
                gamePages.Add(newGamePage);
                newGamePage.Init(gameData);
            }
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }

}
