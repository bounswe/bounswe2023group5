using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class GameScreen : MonoBehaviour
{

    private CanvasManager canvasManager;
    [SerializeField] private Transform gamePageParent;
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnEnable()
    {
        ListGames();
    }


    private void ListGames()
    {
        string url = AppVariables.HttpServerUrl + "/game/get-game-list";
        var gameRequestData = new GetGameRequest();
        string bodyJsonString = JsonConvert.SerializeObject(gameRequestData);
        StartCoroutine(Post(url, bodyJsonString));
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
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
                newGamePage.Init(gameData);
            }
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }

}
