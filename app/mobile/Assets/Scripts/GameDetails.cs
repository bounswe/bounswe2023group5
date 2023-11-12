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
    [SerializeField] private Image gameImage;
    [SerializeField] private Button summaryButton;
    [SerializeField] private Button reviewsButton;
    [SerializeField] private Button forumButton;
    [SerializeField] private TMP_Text headingText;
    [SerializeField] private TMP_Text bottomText;
    [SerializeField] private Button exitButton;
    
    private CanvasManager canvasManager;
    
    private string id;
    private string createdAt;
    private bool isDeleted;
    private string gameName;
    private string gameDescription;
    private string gameIcon;
    private string releaseDate;
    private List<string> playerTypes;
    private List<string> genre;
    private string production;
    private string duration;
    private List<string> platforms;
    private List<string> artStyles;
    private string developer;
    private List<string> otherTags;
    private string minSystemReq;
    
    private void Awake()
    {
        //summaryButton.onClick.AddListener(OnClickedSummaryButton);
        //reviewsButton.onClick.AddListener(OnClickedReviewsButton);
        //forumButton.onClick.AddListener(OnClickedForumButton);
        exitButton.onClick.AddListener(OnClickedExitButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void GameDetail(string gameId)
    {
        
        // Make a request
        string url = AppVariables.HttpServerUrl + "/game/get-game";

        StartCoroutine(Get(url, gameId));
    }

    IEnumerator Get(string url, string gameId)
    {
        var parameteredUrl = url + "?gameId=" + gameId;
        
        // Debug.Log(url);
        
        var request = new UnityWebRequest(parameteredUrl, "GET");
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

            // Set the image 
            StartCoroutine(LoadImageFromURL(AppVariables.HttpServerUrl+ "/" + gameIcon, gameImage));
            
            summaryButton.image.color = Color.blue;
            
            // Set the bottom text
            bottomText.text = gameDescription;
            
            // Set heading
            headingText.text = gameName;
        }
        request.downloadHandler.Dispose();
    }
    
    private void OnClickedExitButton()
    {
        canvasManager.ShowGamesPage();
        canvasManager.HideGameDetailsPage();
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
