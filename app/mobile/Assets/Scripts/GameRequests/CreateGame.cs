using System;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using TMPro;
using Newtonsoft.Json;
using System.Collections;
using System.Text;
using DG.Tweening;

public class CreateGame : MonoBehaviour
{
    [SerializeField] private TMP_InputField gameName;
    [SerializeField] private TMP_InputField gameDescription;
    [SerializeField] private string gameIcon;
    [SerializeField] private string releaseDate;
    [SerializeField] private string[] playerTypes;
    [SerializeField] private string[] genre;
    [SerializeField] private string production;
    [SerializeField] private string[] platforms;
    [SerializeField] private string[] artStyles;
    [SerializeField] private string developer;
    [SerializeField] private string[] otherTags;
    [SerializeField] private string minSystemReq;
    
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

    private void OnClickedCreate()
    {
        
        string url = AppVariables.HttpServerUrl + "/game/create";
        var createGameRequest = new CreateGameRequest();
        createGameRequest.gameName = gameName.text;
        createGameRequest.gameDescription = gameDescription.text;
        
        // Lines below will change
        createGameRequest.gameIcon = "gameIcon file";
        createGameRequest.releaseDate = "2023-02-02";
        createGameRequest.playerTypes = new string[1]
        {
            "90d68bd5-285b-434f-9c2c-1a1b637dd83b"
        };
        createGameRequest.genre= new string[1]
        {
            "f17297ca-8497-4a0b-8d00-a1995187cdc5"
        };
        createGameRequest.production = "373a9a0b-af34-40b4-9764-1ef055dcc27f";
        createGameRequest.platforms= new string[3]
        {
            "b2f060aa-a4f7-467c-9519-6570ddcd82ef",
            "d750d4ba-544c-42b1-8967-39b01626c6c2",
            "37a797a0-907d-4579-9d23-d69b898bb63a"
        };
        createGameRequest.artStyles= new string[1]
        {
            "1f3ebcaf-8776-4b9e-957a-d80070616343"
        };
        createGameRequest.developer= "1cea6329-ccb2-4864-bbdf-24d7c1e3d396";
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
    

}