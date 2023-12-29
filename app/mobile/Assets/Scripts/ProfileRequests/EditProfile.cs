using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class EditProfile : MonoBehaviour
{
    [SerializeField] private TMP_InputField username;
    [SerializeField] private bool isPrivate;
    [SerializeField] private string profilePhoto;
    [SerializeField] private Toggle privateToggle;
    [SerializeField] private Toggle publicToggle;
    [SerializeField] private TMP_InputField steamProfile;
    [SerializeField] private TMP_InputField epicGamesProfile;
    [SerializeField] private TMP_InputField xboxProfile;
    private string id;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    [SerializeField] private Button uploadImageButton;
    [SerializeField] private Button editButton;
    [SerializeField] private Image uploadImage;
    private void OnEnable()
    {
        username.text = PersistenceManager.UserName;
        queryParams.Clear();
        uploadImage.sprite = null;
    }

    private void Awake()
    {
        editButton.onClick.AddListener(OnEditButtonClicked);
        uploadImageButton.onClick.AddListener(OnClickedUploadImage);
    }

    public void SetProfilePrivate()
    {
        if (!privateToggle.isOn)
        {
            return;
        }
        publicToggle.isOn = false;
        isPrivate = true;
        Debug.Log(isPrivate);
    }
    public void SetProfilePublic()
    {
        if (!publicToggle.isOn)
        {
            return;
        }
        privateToggle.isOn = false;
        isPrivate = false;
        Debug.Log(isPrivate);
    }

    public void OnEditButtonClicked()
    {
        queryParams.Clear();
        queryParams.Add("id", PersistenceManager.ProfileId);
        string url = AppVariables.HttpServerUrl + "/profile/edit" +
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
        var editProfileRequest = new EditProfileRequest();
        editProfileRequest.username = username.text;
        editProfileRequest.isPrivate = isPrivate;
        editProfileRequest.profilePhoto = profilePhoto;
        editProfileRequest.steamProfile = steamProfile.text;
        editProfileRequest.epicGamesProfile = epicGamesProfile.text;
        editProfileRequest.xboxProfile = xboxProfile.text;
        string bodyJsonString = JsonUtility.ToJson(editProfileRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to edit profile: " + response);
            PersistenceManager.UserName = username.text;
        }
        else
        {
            Debug.Log("Error to edit profile: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
        CanvasManager.instance.ShowProfilePage();
    }
    
    private void OnClickedUploadImage()
    {
        RequestPermissionAsynchronously();
        editButton.interactable = false;
    }
    private async void RequestPermissionAsynchronously( bool readPermissionOnly = false )
    {
        NativeFilePicker.Permission permission = await NativeFilePicker.RequestPermissionAsync( readPermissionOnly );
        if (permission == NativeFilePicker.Permission.Granted || permission == NativeFilePicker.Permission.ShouldAsk)
        {
            Debug.Log( "Permission granted" );
            string path = PickAnImageFile();
        }
        else
        {
            Debug.Log("Permission denied");
            editButton.interactable = true;
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
                editButton.interactable = true;
            }
            else
            {
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
            StartCoroutine(UploadSprite(texture2D, "gameIcon"));
        }
        else
        {
            Debug.Log("Error loading image: " + www.error);
        }
        editButton.interactable = true;
        www.Dispose();
    }
    

    IEnumerator UploadSprite(Texture2D texture, string folder) {
            Texture2D textureNew = texture;
            byte[] imageBytes = textureNew.EncodeToPNG();
            WWWForm form = new WWWForm();
            form.AddBinaryData("image", imageBytes) ;

            UnityWebRequest www = UnityWebRequest.Post($"{AppVariables.HttpServerUrl}/image/upload?folder={folder}", form);
            yield return www.SendWebRequest();
 
            if(www.isNetworkError || www.isHttpError) {
                Debug.Log(www.error);
                uploadImage.sprite = null;
            }
            else {
                profilePhoto = www.downloadHandler.text;
                Debug.Log("Form upload complete! " + profilePhoto);
            }
            www.downloadHandler.Dispose();
            www.Dispose();
            editButton.interactable = true;
    }
    
}
