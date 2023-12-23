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
    
    [SerializeField] private MultySelectDopdown playerTypes;
    [SerializeField] private MultySelectDopdown genre;
    [SerializeField] private TMP_Dropdown production;
    [SerializeField] private MultySelectDopdown platforms;
    [SerializeField] private MultySelectDopdown artStyles;
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
        StartCoroutine(GetAllTags(AppVariables.HttpServerUrl + "/tag/get-all"));
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
                break;
            case FilterType.Genre:
                break;
            case FilterType.Production:
                if (queryParams.ContainsKey("production"))
                {
                    queryParams["production"] = productionNameArray[production.value];
                }
                else
                {
                    queryParams.Add("production", productionNameArray[production.value]);
                }
                break;
            case FilterType.Platforms:
                break;
            case FilterType.ArtStyles:
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
    
    private List<(string,string)> playerTypesArray = new List<(string,string)>();
    private List<(string,string)> genreArray = new List<(string,string)>();
    private List<(string,string)> platformsArray = new List<(string,string)>();
    private List<(string,string)> artStylesArray = new List<(string,string)>();
    private List<(string,string)> productionArray = new List<(string,string)>();
    private List<string> productionNameArray = new List<string>();

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
                        playerTypesArray.Add((tagResponse.name,tagResponse.id));
                        break;
                    case "GENRE":
                        genreArray.Add((tagResponse.name,tagResponse.id));
                        break;
                    case "PLATFORM":
                        platformsArray.Add((tagResponse.name,tagResponse.id));
                        break;
                    case "ART_STYLE":
                        artStylesArray.Add((tagResponse.name,tagResponse.id));
                        break;
                    case "PRODUCTION":
                        productionArray.Add((tagResponse.name,tagResponse.id));
                        productionNameArray.Add(tagResponse.name);
                        break;
                    default:
                        break;
                }
            }
            Debug.Log("Success to get tags: " + response);
            PopulateMultiSelectDropdown(playerTypes, playerTypesArray);
            PopulateMultiSelectDropdown(genre, genreArray);
            PopulateDropdown(production, productionNameArray);
            PopulateMultiSelectDropdown(platforms, platformsArray);
            PopulateMultiSelectDropdown(artStyles, artStylesArray);
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
    
    void PopulateMultiSelectDropdown (MultySelectDopdown dropdown, List<(string,string)> options) {
        dropdown.InitDropdown(options);
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
