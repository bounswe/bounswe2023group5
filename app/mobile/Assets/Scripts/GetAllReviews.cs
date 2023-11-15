using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class GetAllReviews : MonoBehaviour
{
    [SerializeField] private Transform reviewPageParent;
    private string gameId;
    private List<GameReview> gameReviews = new List<GameReview>();
    public void Init(string _gameID)
    {
        gameId = _gameID;
        GetGameReviews();
    }

    public void GetGameReviews()
    {
        string url = AppVariables.HttpServerUrl + "/review/get-all?id=" + gameId;
        var reviewRequestData = new ReviewGetAllRequest();
        reviewRequestData.gameId = gameId;
        string bodyJsonString = JsonConvert.SerializeObject(reviewRequestData);
        StartCoroutine(Post(url, bodyJsonString));
    }
    
    IEnumerator Post(string url, string bodyJsonString)
    {
        foreach (var gameReview in gameReviews)
        {
            Destroy(gameReview.gameObject);
        }
        gameReviews.Clear();
        var request = new UnityWebRequest(url, "GET");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        var _reviewData = JsonConvert.DeserializeObject<ReviewResponse[]>(response);
        if (request.responseCode == 200)
        {
            foreach (var reviewData in _reviewData)
            {
                GameReview newGamePage = Instantiate(Resources.Load<GameReview>("Prefabs/GameReview"),reviewPageParent);
                gameReviews.Add(newGamePage);
                newGamePage.Init(reviewData);
            }
            Debug.Log("Success to get all review: " + response);
        }
        else
        {
            Debug.Log("Error to get all review: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
