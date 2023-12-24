using System;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using TMPro;
using Newtonsoft.Json;
using System.Collections;
using System.Collections.Generic;
using System.Net;
using System.Text;

public class CreateCharacter : MonoBehaviour
{
    [SerializeField] private Button uploadImageButton;
    [SerializeField] private Image uploadImage;

    [SerializeField] private TMP_InputField name;
    [SerializeField] private TMP_InputField birthDate;
    [SerializeField] private TMP_InputField type;
    [SerializeField] private TMP_InputField gender;
    [SerializeField] private TMP_InputField race;
    [SerializeField] private TMP_InputField status;
    [SerializeField] private TMP_InputField occupation;
    [SerializeField] private TMP_InputField voiceActor;
    [SerializeField] private TMP_InputField height;
    [SerializeField] private TMP_InputField age;
    [SerializeField] private MultySelectDopdown gamesDropdown;
    
    [SerializeField] private Button createCharacterButton;
    [SerializeField] private TMP_Text infoText;
    
    private List<(string,string)> gamesArray = new List<(string,string)>();
    private bool isImageUploaded;
    
    private CanvasManager canvasManager;
    
    private void Awake()
    {
        Debug.Log("CreateCharacter is awake");
        ClearFields();
        uploadImageButton.onClick.AddListener(OnClickedUploadImage);
        createCharacterButton.onClick.AddListener(OnClickedCreateCharacter);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void Start()
    {
        ListGames(null);
        Init();
    }

    public void Init()
    {
        PopulateMultiSelectDropdown(gamesDropdown, gamesArray);
        isImageUploaded = false;
    }

    private void ClearFields()
    {
        name.text = "";
        birthDate.text = "";
        type.text = "";
        gender.text = "";
        race.text = "";
        status.text = "";
        occupation.text = "";
        voiceActor.text = "";
        height.text = "";
        age.text = "";
    }
    
    private void OnClickedUploadImage()
    {
        string path = FileController.PickAnImageFile();
        StartCoroutine(LoadImage(path));
    }
    
    IEnumerator LoadImage(string url)
    {
        // Load the image from the specified path
        WWW www = new WWW("file://" + url);
        yield return www;

        // Check for errors during image loading
        if (string.IsNullOrEmpty(www.error))
        {
            Sprite uploadImageSprite = Sprite.Create(www.texture, new Rect(0, 0, www.texture.width, www.texture.height), new Vector2(0.5f, 0.5f));
            uploadImage.sprite = uploadImageSprite;
            Color tempColor = uploadImage.color;
            tempColor.a = 1f;
            uploadImage.color = tempColor;
            isImageUploaded = true;
        }
        else
        {
            Debug.Log("Error loading image: " + www.error);
        }
    }
    
    private void ListGames(GetGameListRequest gameRequestData)
    {
        string url = AppVariables.HttpServerUrl + "/game/get-game-list";

        
        string bodyJsonString = (gameRequestData == null) ? "" :
            JsonConvert.SerializeObject(gameRequestData);

        StartCoroutine(PostGames(url, bodyJsonString));
    }
    
    IEnumerator PostGames(string url, string bodyJsonString)
    {
        
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
                gamesArray.Add((gameData.gameName, gameData.id));
            }
            Debug.Log("Success to list games: " + response);
            PopulateMultiSelectDropdown(gamesDropdown, gamesArray);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    void PopulateMultiSelectDropdown (MultySelectDopdown dropdown, List<(string,string)> options) {
        dropdown.InitDropdown(options);
    }

    private void OnClickedCreateCharacter()
    {
        // If a field is empty
        if (String.IsNullOrEmpty(name.text) || String.IsNullOrEmpty(birthDate.text) ||
            String.IsNullOrEmpty(type.text) || String.IsNullOrEmpty(gender.text) ||
            String.IsNullOrEmpty(race.text) || String.IsNullOrEmpty(status.text) ||
            String.IsNullOrEmpty(occupation.text) || String.IsNullOrEmpty(voiceActor.text) ||
            String.IsNullOrEmpty(height.text) || String.IsNullOrEmpty(age.text) ||
            !isImageUploaded || (gamesDropdown.GetSelectedItems().Count == 0))
        {
            String message = "Unable to create character: Some fields are empty";
            InfoError(message);
            Debug.Log(message);
            return;
        }
        
        string url = AppVariables.HttpServerUrl + "/character/create";
        var createCharacterRequest = new CharacterRequest();
        createCharacterRequest.name = name.text;
        createCharacterRequest.birthDate = birthDate.text;
        createCharacterRequest.type = type.text;
        createCharacterRequest.gender = gender.text;
        createCharacterRequest.race = race.text;
        createCharacterRequest.status = status.text;
        createCharacterRequest.occupation = occupation.text;
        createCharacterRequest.voiceActor = voiceActor.text;
        createCharacterRequest.height = height.text;
        createCharacterRequest.age = age.text;

        string bodyJson = JsonConvert.SerializeObject(createCharacterRequest);

        StartCoroutine(PostCharacter(url, bodyJson));
    }

    IEnumerator PostCharacter(string url, string bodyJson)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJson);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            var _CreateCharResponseData = JsonConvert.DeserializeObject<CharacterResponse>(response);
            Debug.Log("Success to create the character: " + response);
            InfoSuccess("Successfully created the character");
        }
        else
        {
            Debug.Log("Error to create the character: " + response);
            InfoError("Unable to create the character");
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }

    private void InfoError(String errorText)
    {
        infoText.text = errorText;
        infoText.color = Color.red;
    }
    
    private void InfoSuccess(String successText)
    {
        infoText.text = successText;
        infoText.color = Color.green;
    }
}
