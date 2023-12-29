using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class GameAddTag : MonoBehaviour
{
    [SerializeField] private string gameId;
    [SerializeField] private string tagId;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/game/add-tag";
        var gameAddTagRequest = new GameAddTagRequest();
        // gameId can also be taken from the environment, depending on the
        // implementation of this script
        gameAddTagRequest.gameId = gameId;
        gameAddTagRequest.tagId = tagId;
        string bodyJsonString = JsonUtility.ToJson(gameAddTagRequest);
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
            var _GameAddTagResponseData = JsonConvert.DeserializeObject<GameAddTagResponse>(response);

            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}