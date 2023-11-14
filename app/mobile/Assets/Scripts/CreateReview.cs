using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.PlayerLoop;

public class CreateReview : MonoBehaviour
{
    private string reviewDescription;
    private string rating;
    private string gameId;
    
    public void Init(string _gameId)
    {
        gameId = _gameId;
    }
    
    
    public void CreateGameReview(string title, string body)
    {
        string url = AppVariables.HttpServerUrl + "/review/create";
        var reviewCreateRequest = new ReviewCreateRequest();
        reviewCreateRequest.reviewDescription = reviewDescription;
        reviewCreateRequest.rating = rating;
        reviewCreateRequest.gameId = gameId;
        string bodyJsonString = JsonUtility.ToJson(reviewCreateRequest);
        StartCoroutine(CreateGameReviewPost(url, bodyJsonString));
    }
    IEnumerator CreateGameReviewPost(string url, string bodyJsonString)
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
            Debug.Log(response);
        }
        else
        {
            Debug.Log("error");
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
