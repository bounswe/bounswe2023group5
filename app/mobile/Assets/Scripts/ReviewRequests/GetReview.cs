using System;
using System.Collections;
using System.Text;
using Newtonsoft.Json;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;

public class GetReview : MonoBehaviour
{
    private string reviewId;
    [SerializeField] private TMP_Text id;
    [SerializeField] private TMP_Text reviewDescription;
    [SerializeField] private TMP_Text rating;
    [SerializeField] private TMP_Text gameId;
    [SerializeField] private TMP_Text reviewedUser;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text reportNum;
    [SerializeField] private TMP_Text createdAt;
    

    private void Start()
    {
        Init("64c79919-6623-45ef-b079-e67841d3e79e");
    }

    public void Init(string _reviewId)
    {
        reviewId = _reviewId;
        string url = AppVariables.HttpServerUrl + "/review/get"+"?id=" + reviewId;
        StartCoroutine(GET(url));
    }

    IEnumerator GET(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        var _reviewData = JsonConvert.DeserializeObject<ReviewResponse>(response);
        if (request.responseCode == 200)
        {
            Debug.Log("Success to get review: " + response);
            // id.text = _reviewData.id;
            // reviewDescription.text = _reviewData.reviewDescription;
            // rating.text = _reviewData.rating;
            // gameId.text = _reviewData.gameId;
            // reviewedUser.text = _reviewData.reviewedUser;
            // overallVote.text = _reviewData.overallVote;
            // reportNum.text = _reviewData.reportNum;
            // createdAt.text = _reviewData.createdAt;
        }
        else
        {
            Debug.Log("Error to get review: " + response);
        }
        request.downloadHandler.Dispose();
    }
}