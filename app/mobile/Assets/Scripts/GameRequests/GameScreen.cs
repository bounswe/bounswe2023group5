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
    // private List<(string,string)> queryParams = new List<(string, string)>();

    private void Awake()
    {
        filterButton.onClick.AddListener(OnClickedFilter);
    }

    private void Start()
    {
        GetGameListRequest getGameListRequest = new GetGameListRequest();
        getGameListRequest.findDeleted = false;
        ListGames(getGameListRequest);
        StartCoroutine(GetAllTags(AppVariables.HttpServerUrl + "/tag/get-all"));
    }
    

    private void ListGames(GetGameListRequest gameRequestData)
    {
        string url = AppVariables.HttpServerUrl + "/game/get-game-list";

        
        string bodyJsonString = (gameRequestData == null) ? "" :
            JsonConvert.SerializeObject(gameRequestData);

        StartCoroutine(Post(url, bodyJsonString));
    }

    private void OnClickedFilter()
    {
        GetGameListRequest gameRequestData = ChangeFilterParameter();
        ListGames(gameRequestData);
    }

    private GetGameListRequest ChangeFilterParameter()
    {
        GetGameListRequest gameRequestData = new GetGameListRequest();
        gameRequestData.findDeleted = false;
        var playerTypesList = playerTypes.GetSelectedItems();
        var genreList = genre.GetSelectedItems();
        var platformsList = platforms.GetSelectedItems();
        var artStylesList = artStyles.GetSelectedItems();
        if (playerTypesList.Count > 0)
        {
            gameRequestData.playerTypes = playerTypesList.ConvertAll(x => x.Item2).ToArray();
        }
        if (genreList.Count > 0)
        {
            gameRequestData.genre = genreList.ConvertAll(x => x.Item2).ToArray();
        }
        if (platformsList.Count > 0)
        {
            gameRequestData.platform = platformsList.ConvertAll(x => x.Item2).ToArray();
        }
        if (artStylesList.Count > 0)
        {
            gameRequestData.artStyle = artStylesList.ConvertAll(x => x.Item2).ToArray();
        }
        if (production.value != 0)
        {
            var productionValue = productionArray[production.value].Item2;
            gameRequestData.production = productionValue;
        }
        if (search.text != "")
        {
            gameRequestData.search = search.text;
        }
        return gameRequestData;
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
            productionArray.Add(("All",""));
            productionNameArray.Add("All");
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
