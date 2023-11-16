using System;
using System.Collections;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class DeleeReview : MonoBehaviour
{
    private string reviewId;

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
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success to delete review: " + response);
        }
        else
        {
            Debug.Log("Error to delete review: " + response);
        }
        request.downloadHandler.Dispose();
    }
}