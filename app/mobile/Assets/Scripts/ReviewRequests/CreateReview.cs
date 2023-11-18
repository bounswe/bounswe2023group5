using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.PlayerLoop;

public class CreateReview : MonoBehaviour
{
    [SerializeField] private string reviewDescription;
    [Range(0, 5)] [SerializeField] private int rating;
    private string gameId;

    private void Start()
    {
        Init("083e4af6-5843-44c5-acce-3fcfbb5b024f");
    }

    public void Init(string _gameId)
    {
        gameId = _gameId;
        string url = AppVariables.HttpServerUrl + "/review/create";
        var reviewCreateRequest = new ReviewCreateRequest();
        reviewCreateRequest.reviewDescription = reviewDescription;
        reviewCreateRequest.rating = rating.ToString();
        reviewCreateRequest.gameId = gameId;
        string bodyJsonString = JsonUtility.ToJson(reviewCreateRequest);
        StartCoroutine(POST(url, bodyJsonString));
    }
    
    IEnumerator POST(string url, string bodyJsonString)
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
            Debug.Log("Success to create review: " + response);
        }
        else
        {
            Debug.Log("Error to create review: " + response);
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
