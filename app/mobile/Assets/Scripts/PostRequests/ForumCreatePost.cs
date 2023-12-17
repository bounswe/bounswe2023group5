using System;
using System.Collections;
using System.Text;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class ForumCreatePost : MonoBehaviour
{
    [SerializeField] private TMP_InputField title;
    [SerializeField] private TMP_InputField postContent;
    [SerializeField] private string postImage;
    [SerializeField] private string forumID;
    [SerializeField] private string[] tags;
    [SerializeField] private TMP_Text infoText;
    [SerializeField] private Button createPost;
    [SerializeField] private Button editPost;
    [SerializeField] private Button exit;


    // This will be used in post edit
    private string postId;
    private bool isPageModeCreate;
    
    private CanvasManager canvasManager;
    
    
    private void Start()
    {
        // Init("b4036d6f-0e69-4df3-a935-a84750dc2bcd");
    }

    private void Awake()
    {
        // commentManager = FindObjectOfType(typeof(openComment)) as openComment;
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        createPost.onClick.AddListener(OnClickedCreatePost);
        editPost.onClick.AddListener(OnClickedEditPost);
        exit.onClick.AddListener(OnClickedExit);
    }

    public void Init(string _forumID)
    {
        forumID = _forumID;
        title.text = "";
        postContent.text = "";
        tags = Array.Empty<String>();
        infoText.text = "";
        PageMode("create");
    }
    
    public void Init(string _postID, GetPostListResponse postInfoVal)
    {
        postId = _postID;
        title.text = postInfoVal.title;
        postContent.text = postInfoVal.postContent;
        tags = Array.Empty<String>();
        PageMode("edit");
    }

    public void PageMode(String mode)
    {
        if (mode == "create")
        {
            isPageModeCreate = true;
            createPost.gameObject.SetActive(true);
            editPost.gameObject.SetActive(false);
        }else if (mode == "edit")
        {
            isPageModeCreate = false;
            createPost.gameObject.SetActive(false);
            editPost.gameObject.SetActive(true);
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
        var postCreateRequest = new PostCreateRequestBasic();
        postCreateRequest.title = title.text;
        postCreateRequest.postContent = postContent.text;
        postCreateRequest.postImage = postImage;
        postCreateRequest.forum = forumID;
        postCreateRequest.tags = tags;
        
        string bodyJsonString = JsonUtility.ToJson(postCreateRequest);
        StartCoroutine(PostCreate(url, bodyJsonString));
        
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
}
