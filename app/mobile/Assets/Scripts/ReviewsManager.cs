using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class ReviewsManager : MonoBehaviour
{
    [SerializeField] private Transform reviewPageParent;
    private string gameId;
    public void Init(string _gameID)
    {
        gameId = _gameID;
        GetGameReviews();
    }

    public void GetGameReviews()
    {
        string url = AppVariables.HttpServerUrl + "/review/get-all";
        var reviewRequestData = new ReviewGetAllRequest();
        reviewRequestData.gameId = gameId;
        string bodyJsonString = JsonConvert.SerializeObject(reviewRequestData);
        StartCoroutine(Post(url, bodyJsonString));
    }
    
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "GET");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log(response);
        var _reviewData = JsonConvert.DeserializeObject<ReviewGetAllResponse[]>(response);
        if (request.responseCode != 200 || _reviewData == null)
        {
            Debug.Log("error");
        }
        else
        {
            foreach (var reviewData in _reviewData)
            {
                GameReview newGamePage = Instantiate(Resources.Load<GameReview>("Prefabs/GameReview"),reviewPageParent);
                newGamePage.Init(reviewData);
            }
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
