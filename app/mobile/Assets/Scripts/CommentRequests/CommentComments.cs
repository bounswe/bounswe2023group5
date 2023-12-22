using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;
using TMPro;
using UnityEngine.UI;
using static CommentController;

public class CommentComments : MonoBehaviour
{
    //[SerializeField] private string title;
    //[SerializeField] private string postContent;
    //[SerializeField] private ForumPostDetail forumPost;
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform commentParent;
    private List<CommentBox> commentPages = new List<CommentBox>();
    // [SerializeField] private string postID;
    [SerializeField] private string commentID;
    private CanvasManager canvasManager;
    [SerializeField] private TMP_InputField commentInputField;
    [SerializeField] private Button addCommentButton;
    [SerializeField] private Button exitButton;
    [SerializeField] private TMP_Text infoText;
    private PostComment commentInfo;
    [SerializeField] private Image userImage;
    // [SerializeField] private TMP_Text poster;
    // [SerializeField] private TMP_Text title;
    // [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text commentContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text userName;
    private CommentReply[] replies;

    
    private void Awake()
    {
        addCommentButton.onClick.AddListener(addComment);
        exitButton.onClick.AddListener(exit);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(string id, PostComment postInfoVal )
    {
        commentID = id;
        infoText.text = "";
        commentInfo = postInfoVal;
        replies = commentInfo.replies;
        
        Debug.Log("replies: "+ replies);
        
        commentContent.text = commentInfo.commentContent;
        lastEditedAt.text = commentInfo.lastEditedAt;
        overallVote.text = Convert.ToString(commentInfo.overallVote);
        if (commentInfo.commenter == null)
        {
            userName.text = "(anonymous)";
        }
        else
        {
            userName.text = commentInfo.commenter.username;

        }

        // Comments do not have tags
        
        if (string.IsNullOrEmpty(commentID))
        {
            Debug.Log("Id must be specified");
            return;
        }

        DisplayComments();
    }
    
    // Displays the reply comments
    private void DisplayComments ()
    {
        foreach (var commentPage in commentPages)
        {
            Destroy(commentPage.gameObject);
        }
        commentPages.Clear();
        
        
        
        foreach (var comment in replies)
        {
            if (comment.isDeleted)
            {
                Debug.Log("deleted comment reply is:\n" + comment);
                continue;
            }
            Debug.Log("comment reply is:\n" + comment);
            CommentBox newComment = Instantiate(Resources.Load<CommentBox>("Prefabs/CommentPage"), commentParent);
            commentPages.Add(newComment);
            newComment.Init(comment);
        }
        Canvas.ForceUpdateCanvases();
        scrollRect.verticalNormalizedPosition = 1;
        
    }

    private void addComment()
    {
        CommentCreateRequest req = new CommentCreateRequest();
        if (string.IsNullOrWhiteSpace(commentInputField.text))
        {
            string message = "Comment content cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            infoText.color = Color.red;
            return;
        }
        req.commentContent = commentInputField.text;
        req.post = commentID;
        
        CreateComment(req);
    }

    private void exit()
    {
        canvasManager.HideCommentComments();
    }
    
    
    
    public void ReplyToComment(CommentReplyRequest commentReplyRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/reply";
        string bodyJsonString = JsonConvert.SerializeObject(commentReplyRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }

    public void EditComment(CommentEditRequest commentEditRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/edit";
        string bodyJsonString = JsonConvert.SerializeObject(commentEditRequest);
        StartCoroutine(Put(url, bodyJsonString));
    }

    public void CreateComment(CommentCreateRequest commentCreateRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/create";
        string bodyJsonString = JsonConvert.SerializeObject(commentCreateRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }

    public void DeleteComment(string commentId)
    {
        string url = AppVariables.HttpServerUrl + "/comment/delete?id=" + commentId;
        StartCoroutine(Delete(url));
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
        HandleResponseComment(request);
    }

    IEnumerator Put(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "PUT");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        HandleResponse(request);
    }

    IEnumerator Delete(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        HandleResponse(request);
    }
    
    private void HandleResponseComment(UnityWebRequest request)
    {
        string response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success: " + response);
            infoText.text = "Success to create comment";
            infoText.color = Color.green;
            commentInputField.text = "";
        }
        else
        {
            Debug.LogError("Error: " + response);
        }
        request.downloadHandler.Dispose();
    }

    private void HandleResponse(UnityWebRequest request)
    {
        string response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success: " + response);
        }
        else
        {
            Debug.LogError("Error: " + response);
        }
        request.downloadHandler.Dispose();
    }
}