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
    [SerializeField] private TMP_InputField description;
    
    [SerializeField] private Button createCharacterButton;
    [SerializeField] private TMP_Text infoText;
    
    private List<(string,string)> gamesArray = new List<(string,string)>();
    private bool isImageUploaded;
    
    private CanvasManager canvasManager;

    private string imageFileName;

    [SerializeField] private Button exitButton;
    
    private void Awake()
    {
        Debug.Log("CreateCharacter is awake");
        ClearFields();
        uploadImageButton.onClick.AddListener(OnClickedUploadImage);
        createCharacterButton.onClick.AddListener(OnClickedCreateCharacter);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        exitButton.onClick.AddListener(exit);
    }

    private void Start()
    {
        ListGames(null);
        Init();
    }

    public void Init()
    {
        imageFileName = "";
        ClearFields();
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
        RequestPermissionAsynchronously();
        
    }
    
    private async void RequestPermissionAsynchronously( bool readPermissionOnly = false )
    {
        NativeFilePicker.Permission permission = await NativeFilePicker.RequestPermissionAsync( readPermissionOnly );
        if (permission == NativeFilePicker.Permission.Granted || permission == NativeFilePicker.Permission.ShouldAsk)
        {
            Debug.Log( "Permission granted" );
            string path = PickAnImageFile();
            infoText.text = "path 1: " + path;
        }
        else
        {
            Debug.Log("Permission denied");
            infoText.text = "permission denied";
        }
    }
    
    public string PickAnImageFile()			
    {
        // Don't attempt to import/export files if the file picker is already open
        if( NativeFilePicker.IsFilePickerBusy() )
            return "";
        string _path = "";
        // Pick a PDF file
        string permission = NativeFilePicker.PickFile( ( path ) =>
        {
            if (path == null)
            {
                _path = "null";
                Debug.Log("Operation cancelled");
            }
            else
            {
                infoText.text = "path 2: " + path;
                _path = path;
                StartCoroutine(LoadImage(path));
            }
        }, new string[] { "image/*" } );
        Debug.Log( "Permission result: " + permission );
        return permission;
    }
    private Texture2D texture2D;
    IEnumerator LoadImage(string url)
    {
        // Load the image from the specified path
        WWW www = new WWW("file://" + url);
        yield return www;

        // Check for errors during image loading
        if (string.IsNullOrEmpty(www.error))
        {
            texture2D = www.texture;
            Sprite uploadImageSprite = Sprite.Create(www.texture, new Rect(0, 0, www.texture.width, www.texture.height), new Vector2(0.5f, 0.5f));
            uploadImage.sprite = uploadImageSprite;
            Color tempColor = uploadImage.color;
            tempColor.a = 1f;
            uploadImage.color = tempColor;
            StartCoroutine(UploadSprite(texture2D, "character-icons"));
        }
        else
        {
            Debug.Log("Error loading image: " + www.error);
        }
    }
    /*
    public class ImageUploadRequest
    {
        public string image;
    }
    
    IEnumerator ImagePost(Texture2D texture, string folder)
    {
        Texture2D textureNew = texture;
        byte[] imageBytes = textureNew.EncodeToPNG();
        string imageString = Convert.ToBase64String(imageBytes);
        
        ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
        imageUploadRequest.image = imageString;
        string bodyJsonString = JsonUtility.ToJson(imageUploadRequest);
        var request = new UnityWebRequest($"{AppVariables.HttpServerUrl}/image/upload?folder={folder}", "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            var _CreateGameResponseData = JsonConvert.DeserializeObject<GameDetail>(response);
            Debug.Log("Success to upload image: " + response);
        }
        else
        {
            Debug.Log("Error to upload image: " + response);

        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
      
    }
    */
    IEnumerator UploadSprite(Texture2D texture, string folder) {
        Texture2D textureNew = texture;
        byte[] imageBytes = textureNew.EncodeToPNG();
        WWWForm form = new WWWForm();
        form.AddBinaryData("image", imageBytes) ;

        UnityWebRequest www = UnityWebRequest.Post($"{AppVariables.HttpServerUrl}/image/upload?folder={folder}", form);
        yield return www.SendWebRequest();

        imageFileName = www.downloadHandler.text;
        if(www.isNetworkError || www.isHttpError) {
            Debug.Log(www.error);
        }
        else
        {
            isImageUploaded = true;
            Debug.Log("Form upload complete!");
        }
        www.downloadHandler.Dispose();
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
            String.IsNullOrEmpty(description.text) ||
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
        createCharacterRequest.icon = imageFileName;
        createCharacterRequest.birthDate = birthDate.text;
        createCharacterRequest.type = type.text;
        createCharacterRequest.gender = gender.text;
        createCharacterRequest.race = race.text;
        createCharacterRequest.status = status.text;
        createCharacterRequest.occupation = occupation.text;
        createCharacterRequest.voiceActor = voiceActor.text;
        createCharacterRequest.height = height.text;
        createCharacterRequest.age = age.text;
        createCharacterRequest.description = description.text;

        List<string> name_ids = new List<string>();
        
        foreach (var name_id in gamesDropdown.GetSelectedItems())
        {
            name_ids.Add(name_id.Item2);
        }

        createCharacterRequest.games = name_ids.ToArray();

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

    private void exit()
    {
        canvasManager.HideCreateCharacterPage();
    }
}
