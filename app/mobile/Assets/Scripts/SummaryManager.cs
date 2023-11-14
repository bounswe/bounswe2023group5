using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class SummaryManager : MonoBehaviour
{
    [SerializeField] private Image gameImage;
    [SerializeField] private TMP_Text headingText;
    [SerializeField] private TMP_Text bottomText;
    private string gameId;
    
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
    
    
    // Start is called before the first frame update
    public void Inıt(string _gameId)
    {
        gameId = _gameId;
        GetGameSummary();
    }

    private void GetGameSummary()
    {
        // Make a request
        string url = AppVariables.HttpServerUrl + "/game/get-game";

        StartCoroutine(Get(url));
    }

    IEnumerator Get(string url)
    {
        var parameteredUrl = url + "?gameId=" + gameId;
        
        // Debug.Log(url);
        
        var request = new UnityWebRequest(parameteredUrl, "GET");
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
