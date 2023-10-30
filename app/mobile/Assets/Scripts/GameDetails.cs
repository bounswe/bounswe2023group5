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
    
    private string id;
    private string createdAt;
    private bool isDeleted;
    private string gameName;
    private string gameDescription;
    private string gameIcon;
    private string releaseDate;
    private List<AddedTag> playerTypes;
    private List<AddedTag> genre;
    private AddedTag production;
    private AddedTag duration;
    private List<AddedTag> platforms;
    private List<AddedTag> artStyles;
    private AddedTag developer;
    private List<AddedTag> otherTags;
    private string minSystemReq;
    
    private void Awake()
    {
        //summaryButton.onClick.AddListener(OnClickedSummaryButton);
        //reviewsButton.onClick.AddListener(OnClickedReviewsButton);
        //forumButton.onClick.AddListener(OnClickedForumButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        
        GameDetail("dsgfhjhgds");
    }

    public void GameDetail(string gameId)
    {
        
        // Make a request
        string url = AppVariables.HttpServerUrl + "/game/get-game";

        StartCoroutine(Get(url, gameId));
    }

    IEnumerator Get(string url, string gameId)
    {
        url += "?gameId=" + gameId;
        
        Debug.Log(url);
        
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        

        yield return request.SendWebRequest();

        var response = request.downloadHandler.text;

        var _gamesData = JsonConvert.DeserializeObject<GetGameResponse>(response);
        

        if (request.responseCode != 200 || _gamesData == null)
        {
            bottomText.text = "Error: " + request.responseCode;
            bottomText.color = Color.red;
        }
        else
        {
            // Set all the variables corresponding to fields
            id = _gamesData.game.id;
            createdAt = _gamesData.game.createdAt;
            isDeleted = _gamesData.game.isDeleted;
            gameName = _gamesData.game.gameName;
            gameDescription = _gamesData.game.gameDescription;
            gameIcon = _gamesData.game.gameIcon;
            releaseDate = _gamesData.game.releaseDate;
            playerTypes = _gamesData.game.playerTypes;
            genre = _gamesData.game.genre;
            production = _gamesData.game.production;
            duration = _gamesData.game.duration;
            platforms = _gamesData.game.platforms;
            artStyles = _gamesData.game.artStyles;
            developer = _gamesData.game.developer;
            otherTags = _gamesData.game.otherTags;
            minSystemReq = _gamesData.game.minSystemReq;

            summaryButton.image.color = Color.blue;
            bottomText.text = gameDescription;
        }
    }
}
