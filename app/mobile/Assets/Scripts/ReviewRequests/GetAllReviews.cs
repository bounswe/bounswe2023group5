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
    public void Init(string[] pars, string[] vals)
    {
        gameId = ListToQueryParameters.GetValueOfParam(
            pars, vals, "id" );
        
        if (gameId == "")
        {
            Debug.Log("Id must be specified");
        }
        
        GetGameReviews(pars, vals);
    }

    public void GetGameReviews(string[] pars, string[] vals)
    {

        string url = AppVariables.HttpServerUrl + "/review/get-all" + 
                     ListToQueryParameters.ListToQueryParams(pars,vals);
        /*
        var reviewRequestData = new ReviewGetAllRequest();
        reviewRequestData.gameId = gameId;
        string bodyJsonString = JsonConvert.SerializeObject(reviewRequestData);
        */

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
        // byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        // request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);

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
