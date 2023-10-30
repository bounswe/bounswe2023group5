using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.UI;

public class GameDetails : MonoBehaviour
{
    [SerializeField] private Button summaryButton;
    [SerializeField] private Button reviewsButton;
    [SerializeField] private Button forumButton;
    [SerializeField] private TMP_Text headingText;
    [SerializeField] private TMP_Text bottomText;
    private CanvasManager canvasManager;

    public class GameResponse
    {
        public string gameName;
        public string gameDescription;
        public string gameIcon;
    }

    private void Awake()
    {
        //summaryButton.onClick.AddListener(OnClickedSummaryButton);
        //reviewsButton.onClick.AddListener(OnClickedReviewsButton);
        //forumButton.onClick.AddListener(OnClickedForumButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        
        // Make a request
        string url = AppVariables.HttpServerUrl + "/game/get-game-list";

        StartCoroutine(Get(url, "sth"));
    }

    IEnumerator Get(string url, string gameName)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");

        yield return request.SendWebRequest();

        var response = request.downloadHandler.text;

        var _gamesData = JsonConvert.DeserializeObject<List<GameResponse>>(response);
        

        if (request.responseCode != 200 || _gamesData == null)
        {
            bottomText.text = "Error: " + request.responseCode;
            bottomText.color = Color.red;
        }
        else
        {
            Debug.Log(_gamesData[0].gameName);
        }
    }
}
