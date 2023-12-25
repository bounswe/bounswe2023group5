using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;
using System.Linq;

public class ForumCreatePost : MonoBehaviour
{
    [SerializeField] private TMP_InputField title;
    [SerializeField] private TMP_InputField postContent;
    [SerializeField] private string postImage;
    [SerializeField] private string forumID;
    [SerializeField] private List<string> tags = new List<string>();
    [SerializeField] private TMP_Text infoText;
    [SerializeField] private Button createPost;
    [SerializeField] private Button editPost;
    [SerializeField] private Button exit;
    [SerializeField] private MultySelectDopdown tagDropdown;
    
    [SerializeField] private GameObject titlePanel;
    [SerializeField] private GameObject contentPanel;
    [SerializeField] private GameObject tagPanel;
    [SerializeField] private GameObject characterPanel;
    
    [SerializeField] private ScrollRect charScroll;
    [SerializeField] private Transform charObjParent;
    
    // This will be used in post edit
    private string postId;
    private string gameId;
    private bool isPageModeCreate;
    
    /*
    private bool hasChar;
    private bool hasAchievement;
    */
    
    private List<(string,string)> postTypesArray = new List<(string,string)>();
    private List<Character> charObjects = new List<Character>();
    
    private CanvasManager canvasManager;
    

    private void Awake()
    {
        // commentManager = FindObjectOfType(typeof(openComment)) as openComment;
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        createPost.onClick.AddListener(OnClickedCreatePost);
        editPost.onClick.AddListener(OnClickedEditPost);
        exit.onClick.AddListener(OnClickedExit);
    }
    private void Start()
    {
        StartCoroutine(GetAllTags(AppVariables.HttpServerUrl + "/tag/get-all"));
    }

    // Create mode
    public void Init(string _forumID, string _gameId)
    { 
        /*
        hasChar = false;
        hasAchievement = false;
        */
        
        forumID = _forumID;
        gameId = _gameId;
        title.text = "";
        postContent.text = "";
        //tags = Array.Empty<String>();
        infoText.text = "";
        PopulateMultiSelectDropdown(tagDropdown, postTypesArray);
        PageMode("create");
    }
    
    // Edit mode
    public void Init(string _postID, GetPostListResponse postInfoVal)
    {
        postId = _postID;
        title.text = postInfoVal.title;
        postContent.text = postInfoVal.postContent;
        //tags = Array.Empty<String>();
        infoText.text = "";
        PageMode("edit");
    }
    

    public void PageMode(String mode)
    {
        if (mode == "create")
        {
            isPageModeCreate = true;
            // Buttons
            createPost.gameObject.SetActive(true);
            editPost.gameObject.SetActive(false);
            
            // Panels
            titlePanel.SetActive(true);
            contentPanel.SetActive(true);
            tagPanel.SetActive(true);
            characterPanel.SetActive(true);
            
            ShowCharacters();
        }else if (mode == "edit")
        {
            isPageModeCreate = false;
            // Buttons
            createPost.gameObject.SetActive(false);
            editPost.gameObject.SetActive(true);
            
            // Panels
            titlePanel.SetActive(true);
            contentPanel.SetActive(true);
            tagPanel.SetActive(false);
            characterPanel.SetActive(false);
        }
        else
        {
            Debug.Log("There is no page mode as: \"" + mode + "\"");
        }
    }

    private void OnClickedCreatePost()
    {
        if (title.text == "")
        {
            String message = "Title cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            return;
        }

        if (postContent.text == "")
        {
            String message = "Content cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            return;
        }
        
        string url = AppVariables.HttpServerUrl + "/post/create";
        // This will be CHANGED to PostCreateRequest
        var postCreateRequest = new PostCreateRequest();
        postCreateRequest.title = title.text;
        postCreateRequest.postContent = postContent.text;
        postCreateRequest.postImage = postImage;
        postCreateRequest.forum = forumID;
        GetTags();
        postCreateRequest.tags = tags.ToArray();
        int numChar = CheckCharacters();
        if (numChar > 1)
        {
            InfoError("Please select only a single character or none");
            postCreateRequest.character = null;
            return;
        }
        else if (numChar == 1)
        {
            Debug.Log("Successfully adding character");
            postCreateRequest.character = GetCheckedCharacter();
            Debug.Log("hey");
        }
        else
        {
            Debug.Log("No character is selected, therefore adding none");
        }
        // No achievement is selected
        postCreateRequest.achievement = null;
        string bodyJsonString = JsonConvert.SerializeObject(postCreateRequest, Formatting.Indented);
        StartCoroutine(PostCreate(url, bodyJsonString));
        
    }

    private int CheckCharacters()
    {
        int numChar = 0;
        for (int i = 0; i < charObjects.Count; i++)
        {
            if (charObjects[i].IsAdded())
            {
                numChar++;
            }
        }
         return numChar;
    }

    private string GetCheckedCharacter()
    {
        for (int i = 0; i < charObjects.Count; i++)
        {
            if (charObjects[i].IsAdded())
            {
                return charObjects[i].GetInfo();
            }
        }
        Debug.Log("Awaited 1 character but no character is added");

        return "";
    }

    private void GetTags()
    {
        tags = new List<string>();
        foreach (var item in tagDropdown.GetSelectedItems())
        {
            if (item.Item1 == "Post")
            {
                tags.Add(item.Item2);
            }
        }
    }

    private void OnClickedEditPost()
    {
        if (title.text == "")
        {
            String message = "Title cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            return;
        }

        if (postContent.text == "")
        {
            String message = "Content cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            return;
        }
        
        string url = AppVariables.HttpServerUrl + "/post/edit" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{postId});
        // This will be CHANGED to PostCreateRequest
        var postEditRequest = new PostEditRequest();
        postEditRequest.title = title.text;
        postEditRequest.postContent = postContent.text;
        
        string bodyJsonString = JsonUtility.ToJson(postEditRequest);
        StartCoroutine(PostEdit(url, bodyJsonString));
    }
    
    public void OnClickedExit()
    {
        canvasManager.HideCreateEditPostPage();
    }
    

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
                    case "POST":
                        postTypesArray.Add((tagResponse.name,tagResponse.id));
                        break;
                    default:
                        break;
                }
            }
            Debug.Log("Success to get tags: " + response);
            PopulateMultiSelectDropdown(tagDropdown, postTypesArray);
        }
        else
        {
            Debug.Log("Error to get tags: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
    void PopulateMultiSelectDropdown (MultySelectDopdown dropdown, List<(string,string)> options) {
        dropdown.InitDropdown(options);
    }
    
    IEnumerator PostCreate(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success to create forum post: " + response);
            infoText.text = "Success to create forum post";
            infoText.color = Color.green;
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
            infoText.text = "Error to create forum post";
            infoText.color = Color.red;
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    IEnumerator PostEdit(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success to edit forum post: " + response);
            infoText.text = "Success to edit forum post";
            infoText.color = Color.green;
        }
        else
        {
            Debug.Log("Error to edit forum post: " + response);
            infoText.text = "Error to edit forum post";
            infoText.color = Color.red;
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    private void ShowCharacters()
    {
        string url = $"{AppVariables.HttpServerUrl}/character/get-game-characters" +
                     ListToQueryParameters.ListToQueryParams(new[] { "gameId" }, new[] { gameId });

        StartCoroutine(GetCharacters(url));
    }
    
    IEnumerator GetCharacters(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            var charactersResponseData = JsonConvert.DeserializeObject<CharacterResponse[]>(response);

            // Do things with _GetAllTagsResponseData 
            DisplayCharacters(charactersResponseData);
            Debug.Log("Success to list characters: " + response);
        }
        else
        {
            Debug.Log("Error to list characters: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
    private void DisplayCharacters(CharacterResponse[] characters)
    {
        foreach (var charObj in charObjects)
        {
            Destroy(charObj.gameObject);
        }
        charObjects.Clear();

        foreach (var character in characters)
        {
            if (character.isDeleted)
            {
                continue;
            }
            Character charObj = Instantiate(Resources.Load<Character>("Prefabs/Character"), charObjParent);
            charObjects.Add(charObj);
            // will be inited differently
            charObj.Init(character);
        }
        Canvas.ForceUpdateCanvases();
        charScroll.horizontalNormalizedPosition = 1;
        Debug.Log("Success to list characters");
    }

    private void InfoError(string message)
    {
        Debug.Log(message);
        infoText.text = message;
        infoText.color = Color.red;
    }
    
    private void InfoSuccess(string message)
    {
        Debug.Log(message);
        infoText.text = message;
        infoText.color = Color.green;
    }
}
