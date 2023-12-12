using System;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using TMPro;
using Newtonsoft.Json;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;

public class CreateGame : MonoBehaviour
{
    [SerializeField] private TMP_InputField gameName;
    [SerializeField] private TMP_InputField gameDescription;
    [SerializeField] private string gameIcon;
    [SerializeField] private TMP_InputField releaseDate;
    [SerializeField] private TMP_Dropdown playerTypes;
    [SerializeField] private TMP_Dropdown genre;
    [SerializeField] private TMP_Dropdown production;
    [SerializeField] private TMP_Dropdown platforms;
    [SerializeField] private TMP_Dropdown artStyles;
    [SerializeField] private TMP_Dropdown developer;
    
    // public TMP_InputField nameInputField;
    // public TMP_InputField summaryInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button createButton;
    [SerializeField] private TMP_Text infoText;
    
    private void Awake()
    {
        createButton.onClick.AddListener(OnClickedCreate);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void Start()
    {
        Init();
    }

    private void OnClickedCreate()
    {
        
        string url = AppVariables.HttpServerUrl + "/game/create";
        var createGameRequest = new CreateGameRequest();
        createGameRequest.gameName = gameName.text;
        createGameRequest.gameDescription = gameDescription.text;
        
        // Lines below will change
        createGameRequest.gameIcon = "gameIcon file";
        createGameRequest.releaseDate = releaseDate.text;
        createGameRequest.playerTypes = new string[1]
        {
            playerTypesArrayID[playerTypes.value]
        };
        createGameRequest.genre= new string[1]
        {
            genreArrayID[genre.value]
        };
        createGameRequest.production = platformsArrayID[production.value];
        createGameRequest.platforms= new string[1]
        {
            platformsArrayID[platforms.value]
        };
        createGameRequest.artStyles= new string[1]
        {
            artStylesArrayID[artStyles.value]
        };
        createGameRequest.developer= developerArrayID[developer.value];
        createGameRequest.otherTags = new string [0];
        createGameRequest.minSystemReq = "min system requirements";
            
        string bodyJsonString = JsonUtility.ToJson(createGameRequest);
        StartCoroutine(Post(url,bodyJsonString));
       
    }
    
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            var _CreateGameResponseData = JsonConvert.DeserializeObject<GameDetail>(response);
            Debug.Log("Success to create review: " + response);
            infoText.text = "Successfully created the game";
            infoText.color = Color.green;
        }
        else
        {
            Debug.Log("Error to create review: " + response);
            infoText.text = "Unable to create the game";
            infoText.color = Color.red;
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
      
    }
    
    public void Init()
    {
        // We can add any of the query parameters (name, color, tagType, 
        // isDeleted) in ListToQueryParams. Or we may add no query 
        // parameters.
        string url = AppVariables.HttpServerUrl + "/tag/get-all";
        StartCoroutine(GetAllTags(url));
    }
    private List<string> playerTypesArray = new List<string>();
    private List<string> genreArray = new List<string>();
    private List<string> productionArray = new List<string>();
    private List<string> platformsArray = new List<string>();
    private List<string> artStylesArray = new List<string>();
    private List<string> developerArray = new List<string>();
    private List<string> playerTypesArrayID= new List<string>();
    private List<string> genreArrayID = new List<string>();
    private List<string> productionArrayID = new List<string>();
    private List<string> platformsArrayID = new List<string>();
    private List<string> artStylesArrayID = new List<string>();
    private List<string> developerArrayID = new List<string>();
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
                    case "DEVELOPER":
                        developerArray.Add(tagResponse.name);
                        developerArrayID.Add(tagResponse.id);
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
            PopulateDropdown(developer, developerArray);
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
    

}