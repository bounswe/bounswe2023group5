using System;
using System.Collections;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class UpdateReview : MonoBehaviour
{
    [SerializeField] private string reviewDescription;
    [Range(0, 5)][SerializeField] private int rate;
    private string reviewId;

    private void Start()
    {
        Init("64c79919-6623-45ef-b079-e67841d3e79e");
    }

    public void Init(string _reviewId)
    {
        reviewId = _reviewId;
        string url = AppVariables.HttpServerUrl + "/review/update"+"?id=" + reviewId;
        var reviewUpdateBody = new ReviewUpdateRequest();
        reviewUpdateBody.reviewDescription = reviewDescription;
        reviewUpdateBody.rating = rate.ToString();
        string bodyJsonString = JsonConvert.SerializeObject(reviewUpdateBody);
        StartCoroutine(PUT(url, bodyJsonString));
    }

    IEnumerator PUT(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "PUT");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success to update review: " + response);
        }
        else
        {
            Debug.Log("Error to update review: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
