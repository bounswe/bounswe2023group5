using System.Collections;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.UI;

public class ForumCreatePost : MonoBehaviour
{
    [SerializeField] private TMP_InputField titleInputField;
    [SerializeField] private TMP_InputField postContentInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button createPostButton;
    [SerializeField] private Button backButton;
    [SerializeField] private TMP_Text errorText;

    private string forumID;

    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        
        createPostButton.onClick.AddListener(OnCreatePostButtonClicked);
        backButton.onClick.AddListener(OnBackButtonClicked);
    }

    private void Start()
    {
        Init("b4036d6f-0e69-4df3-a935-a84750dc2bcd");
    }

    public void Init(string _forumID)
    {
        forumID = _forumID;
    }

    public void OnCreatePostButtonClicked()
    {
        string title = titleInputField.text;
        string postContent = postContentInputField.text;
        string postImage = "";
        string tags = "";

        if (string.IsNullOrEmpty(title) || string.IsNullOrEmpty(postContent))
        {
            ShowError("Title and post content are required.");
            return;
        }

        string[] tagArray = string.IsNullOrEmpty(tags) ? new string[0] : tags.Split(',');

        string url = AppVariables.HttpServerUrl + "/post/create";
        var postCreateRequest = new PostCreateRequest
        {
            title = title,
            postContent = postContent,
            postImage = postImage,
            forum = forumID,
            tags = tagArray
        };

        string bodyJsonString = JsonUtility.ToJson(postCreateRequest);
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

        if (request.responseCode == 200)
        {
            Debug.Log("Success to create forum post: " + request.downloadHandler.text);
            canvasManager.HideCreatePostPage();
        }
        else
        {
            ShowError("Error: " + request.responseCode);
            Debug.Log("Error to create forum post: " + request.downloadHandler.text);
        }

        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    private void OnBackButtonClicked()
    {
        canvasManager.HideCreatePostPage();
        canvasManager.ShowForumPage();
    }

    private void ShowError(string errorMessage)
    {
        errorText.text = errorMessage;
    }
}
