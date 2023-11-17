using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.Serialization;
using UnityEngine.UI;

public class GameDetails : MonoBehaviour
{

    [SerializeField] private Button summaryButton;
    [SerializeField] private Button reviewsButton;
    [SerializeField] private Button forumButton;
    [SerializeField] private GameObject summaryManager;
    [SerializeField] private GetAllReviews getAllReviews;

    [SerializeField] private ForumGetPostList forumManager;

    [SerializeField] private Button exitButton;
    private string gameId;
    
    private CanvasManager canvasManager;
    
    
    
    [SerializeField] private Image gameImage;
    [SerializeField] private TMP_Text headingText;
    [SerializeField] private TMP_Text bottomText;
    
    private string id;
    private string createdAt;
    private bool isDeleted;
    private string gameName;
    private string gameDescription;
    private string gameIcon;
    private string overallRating;
    private string ratingCount;
    private string releaseDate;
    private string forum;
    private string[] playerTypes;
    private string[] genre;
    private string production;
    private string duration;
    private string[] platforms;
    private string[] artStyles;
    private string developer;
    private string[] otherTags;
    private string minSystemReq;
    

    
    private void Awake()
    {
        summaryButton.onClick.AddListener(OnClickedSummaryButton);
        reviewsButton.onClick.AddListener(OnClickedReviewsButton);
        forumButton.onClick.AddListener(OnClickedForumButton);
        exitButton.onClick.AddListener(OnClickedExitButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(string _gameID)
    {
        gameId = _gameID;
        OnClickedSummaryButton();
        GetGameSummary();
    }

    
    
    private void OnClickedExitButton()
    {
        canvasManager.ShowGamesPage();
        canvasManager.HideGameDetailsPage();
    }
    
    private void OnClickedSummaryButton()
    {
        summaryButton.image.color = Color.blue;
        reviewsButton.image.color = Color.white;
        forumButton.image.color = Color.white;
        
        summaryManager.gameObject.SetActive(true);
        getAllReviews.gameObject.SetActive(false);
        forumManager.gameObject.SetActive(false);
    }
    
    private void OnClickedReviewsButton()
    {
        summaryButton.image.color = Color.white;
        reviewsButton.image.color = Color.blue;
        forumButton.image.color = Color.white;
        
        summaryManager.gameObject.SetActive(false);
        getAllReviews.gameObject.SetActive(true);
        forumManager.gameObject.SetActive(false);
        getAllReviews.Init(new []{"gameId"},new []{gameId});
    }
    
    private void OnClickedForumButton()
    {
        summaryButton.image.color = Color.white;
        reviewsButton.image.color = Color.white;
        forumButton.image.color = Color.blue;
        
        summaryManager.gameObject.SetActive(false);
        getAllReviews.gameObject.SetActive(false);
        forumManager.gameObject.SetActive(true);
        
        // 3 parameters are required
        forumManager.ListForumPosts(new [] {"forum", "sortBy", "sortDirection"},
            new [] {forum, "CREATION_DATE", "ASCENDING"});
    }
    

    private void GetGameSummary()
    {
        // Make a request to game id
        string url = AppVariables.HttpServerUrl + "/game/get-game" + 
                     ListToQueryParameters.ListToQueryParams(
                         new []{"gameId"}, new []{gameId});

        StartCoroutine(Get(url));
    }

    IEnumerator Get(string url)
    {
        // var parameteredUrl = url + "?gameId=" + gameId;
        
        // Debug.Log(url);
        
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        

        yield return request.SendWebRequest();

        var response = request.downloadHandler.text;
        Debug.Log(response);
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
            overallRating = _gamesData.game.overallRating;
            ratingCount = _gamesData.game.ratingCount;
            releaseDate = _gamesData.game.releaseDate;
            forum = "b4036d6f-0e69-4df3-a935-a84750dc2bcd";
            playerTypes = _gamesData.game.playerTypes;
            genre = _gamesData.game.genre;
            production = _gamesData.game.production;
            duration = _gamesData.game.duration;
            platforms = _gamesData.game.platforms;
            artStyles = _gamesData.game.artStyles;
            developer = _gamesData.game.developer;
            otherTags = _gamesData.game.otherTags;
            minSystemReq = _gamesData.game.minSystemReq;

            // Set the image 
            string pictureURL = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
            StartCoroutine(LoadImageFromURL(pictureURL + gameIcon, gameImage));
            
            
            // Set the bottom text
            bottomText.text = gameDescription;
            
            // Set heading
            headingText.text = gameName;
        }
        request.downloadHandler.Dispose();
    }
    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(imageUrl);
        yield return request.SendWebRequest();

        if(request.result != UnityWebRequest.Result.Success)
        {
            // Debug.LogError("Failed to load image: " + request.error);
        }
        else
        {
            Texture2D texture = DownloadHandlerTexture.GetContent(request);
            targetImage.sprite = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }
    }
    
    
}
