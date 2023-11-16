using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GetAllReviews : MonoBehaviour
{
    [SerializeField] private ScrollRect scrollRect;
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
        //todo: add filters
        StartCoroutine(Post(url));
    }
    
    IEnumerator Post(string url)
    {
        foreach (var gameReview in gameReviews)
        {
            Destroy(gameReview.gameObject);
        }
        gameReviews.Clear();
        var request = new UnityWebRequest(url, "GET");
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
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
            Debug.Log("Success to get all review: " + response);
        }
        else
        {
            Debug.Log("Error to get all review: " + response);
        }
        request.downloadHandler.Dispose(); }
}
