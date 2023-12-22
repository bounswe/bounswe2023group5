using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GameScreen : MonoBehaviour
{
    
    [SerializeField] private TMP_Dropdown playerTypes;
    [SerializeField] private TMP_Dropdown genre;
    [SerializeField] private TMP_Dropdown production;
    [SerializeField] private TMP_Dropdown platforms;
    [SerializeField] private TMP_Dropdown artStyles;
    [SerializeField] private TMP_InputField search;

    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform gamePageParent;
    [SerializeField] private Button filterButton;
    
    private List<GamePage> gamePages = new List<GamePage>();
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();


    private void Awake()
    {
        filterButton.onClick.AddListener(OnClickedFilter);
    }

    private void Start()
    {
        ListGames(null);
    }
    

    private void ListGames(GetGameListRequest gameRequestData)
    {
        string url = AppVariables.HttpServerUrl + "/game/get-game-list";

        
        string bodyJsonString = (gameRequestData == null) ? "" :
            JsonConvert.SerializeObject(gameRequestData);

        /*
        var gameRequestData = new GetGameListRequest();
        //todo: add filters
        string bodyJsonString = JsonConvert.SerializeObject(gameRequestData);
        */
        
        StartCoroutine(Post(url, bodyJsonString));
    }

    private void OnClickedFilter()
    {
        
    }

    private void ChangeFilterParameter(TMP_Dropdown dropdown, FilterType filterType)
    {
        switch (filterType)
        {
            case FilterType.PlayerType:
                queryParams.Add("playerType", playerTypesArray[playerTypes.value]);
                break;
            case FilterType.Genre:
                queryParams.Add("genre", genreArray[genre.value]);
                break;
            case FilterType.Production:
                if (queryParams.ContainsKey("production"))
                {
                    queryParams["production"] = productionArray[production.value];
                }
                else
                {
                    queryParams.Add("production", productionArray[production.value]);
                }
                break;
            case FilterType.Platforms:
                queryParams.Add("platform", platformsArray[platforms.value]);
                break;
            case FilterType.ArtStyles:
                queryParams.Add("artStyle", artStylesArray[artStyles.value]);
                break;
            default:
                throw new ArgumentOutOfRangeException(nameof(filterType), filterType, null);
        }
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
        foreach (var gamePage in gamePages)
        {
            Destroy(gamePage.gameObject);
        }
        gamePages.Clear();
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log(response);
        var _gamesData = JsonConvert.DeserializeObject<GameListEntry[]>(response);
        if (request.responseCode != 200 || _gamesData == null)
        {
            Debug.Log("error");
        }
        else
        {
            foreach (var gameData in _gamesData)
            {
                GamePage newGamePage = Instantiate(Resources.Load<GamePage>("Prefabs/GamePage"), gamePageParent);
                gamePages.Add(newGamePage);
                newGamePage.Init(gameData);
            }
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    private List<string> playerTypesArray = new List<string>();
    private List<string> genreArray = new List<string>();
    private List<string> productionArray = new List<string>();
    private List<string> platformsArray = new List<string>();
    private List<string> artStylesArray = new List<string>();
    private List<string> playerTypesArrayID= new List<string>();
    private List<string> genreArrayID = new List<string>();
    private List<string> productionArrayID = new List<string>();
    private List<string> platformsArrayID = new List<string>();
    private List<string> artStylesArrayID = new List<string>();
    IEnumerator GetAllTags(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            var allTagsResponseData = JsonConvert.DeserializeObject<TagResponse[]>(response);

            // Do things with _GetAllTagsResponseData 
            foreach (var tagResponse in allTagsResponseData)
            {
                switch (tagResponse.tagType)
                {
                    case "PLAYER_TYPE":
                        playerTypesArray.Add(tagResponse.name);
                        playerTypesArrayID.Add(tagResponse.id);
                        break;
                    case "GENRE":
                        genreArray.Add(tagResponse.name);
                        genreArrayID.Add(tagResponse.id);
                        break;
                    case "PLATFORM":
                        platformsArray.Add(tagResponse.name);
                        platformsArrayID.Add(tagResponse.id);
                        break;
                    case "ART_STYLE":
                        artStylesArray.Add(tagResponse.name);
                        artStylesArrayID.Add(tagResponse.id);
                        break;
                    case "PRODUCTION":
                        productionArray.Add(tagResponse.name);
                        productionArrayID.Add(tagResponse.id);
                        break;
                    default:
                        break;
                }
            }
            Debug.Log("Success to get tags: " + response);
            PopulateDropdown(playerTypes, playerTypesArray);
            PopulateDropdown(genre, genreArray);
            PopulateDropdown(production, productionArray);
            PopulateDropdown(platforms, platformsArray);
            PopulateDropdown(artStyles, artStylesArray);
        }
        else
        {
            Debug.Log("Error to get tags: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
    void PopulateDropdown (TMP_Dropdown dropdown, List<string> options) {
        dropdown.ClearOptions ();
        dropdown.AddOptions(options);
    }
    
    enum FilterType
    {
        PlayerType,
        Genre,
        Production,
        Platforms,
        ArtStyles,
        Developer
    }

}
