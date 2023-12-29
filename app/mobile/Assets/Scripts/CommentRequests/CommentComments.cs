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
    [SerializeField] private Button editCommentButton;
    [SerializeField] private Button discardCommentButton;
    [SerializeField] private Button exitButton;
    [SerializeField] private TMP_Text infoText;
    private PostComment commentInfo;
    [SerializeField] private Image userImage;
    // [SerializeField] private TMP_Text poster;
    // [SerializeField] private TMP_Text title;
    // [SerializeField] private TMP_Text postContent;
    
    // Fields of parent comment
    [SerializeField] private TMP_Text commentContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text userName;
    [SerializeField] private Button deleteButton;
    [SerializeField] private Button editButton;
    [SerializeField] private Button commentsButton;
    
    private CommentReply[] replies;

    // will be used in reply edit mode
    private string replyId;
    
    private void Awake()
    {
        addCommentButton.onClick.AddListener(addReply);
        editCommentButton.onClick.AddListener(editReply);
        discardCommentButton.onClick.AddListener(discardReply);
        exitButton.onClick.AddListener(exit);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(string id, PostComment commentInfoVal )
    {
        // Page is in add reply mode
        AddReplyMode();
        
        commentID = id;
        infoText.text = "";
        commentInfo = commentInfoVal;
        replies = commentInfo.replies;
        
        Debug.Log("replies: "+ replies);
        
        commentContent.text = commentInfo.commentContent;
        lastEditedAt.text = commentInfo.lastEditedAt.ToString("dd/MM/yyyy");
        overallVote.text = Convert.ToString(commentInfo.overallVote);
        if (commentInfo.commenter == null)
        {
            userName.text = "(anonymous)";
        }
        else
        {
            userName.text = commentInfo.commenter.username;

        }
        
        // On reply page many buttons should be set inactive
        deleteButton.gameObject.SetActive(false);
        editButton.gameObject.SetActive(false);
        commentsButton.gameObject.SetActive(false);

        // Comments do not have tags
        
        if (string.IsNullOrEmpty(commentID))
        {
            Debug.Log("Id must be specified");
            return;
        }

        DisplayReplies();
    }
    
    public void Refresh()
    {
        if (!String.IsNullOrEmpty(commentID) && (commentInfo != null))
        {
            Init(commentID, commentInfo);
        }
    }
    
    // Displays the reply comments
    private void DisplayReplies ()
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
                Debug.Log("deleted comment reply by user \""+ comment.commenter.username+"\" is: " + comment.commentContent);
                continue;
            }
            Debug.Log("comment reply by user \""+ comment.commenter.username+"\" is: " + comment.commentContent);
            CommentBox newComment = Instantiate(Resources.Load<CommentBox>("Prefabs/CommentPage"), commentParent);
            commentPages.Add(newComment);
            newComment.Init(comment, this);
        }
        Canvas.ForceUpdateCanvases();
        scrollRect.verticalNormalizedPosition = 1;
        
    }

    private void addReply()
    {
        CommentReplyRequest req = new CommentReplyRequest();
        if (string.IsNullOrWhiteSpace(commentInputField.text))
        {
            string message = "Reply content cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            infoText.color = Color.red;
            return;
        }
        req.commentContent = commentInputField.text;
        req.parentComment = commentID;
        
        ReplyToComment(req);
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
    
    private void AddReplyMode()
    {
        commentInputField.text = "";
        replyId = "";
        
        // open the add comment button and close the edit comment button
        addCommentButton.gameObject.SetActive(true);
        editCommentButton.gameObject.SetActive(false);
        discardCommentButton.gameObject.SetActive(true);
    }
    
    public void EditReplyMode(CommentReply replyInfo)
    {
        commentInputField.text = replyInfo.commentContent;
        replyId = replyInfo.id;
        
        // close the add comment button and open the edit comment button
        addCommentButton.gameObject.SetActive(false);
        editCommentButton.gameObject.SetActive(true);
        discardCommentButton.gameObject.SetActive(true);
    }
    
    private void editReply()
    {
        CommentEditRequest req = new CommentEditRequest();
        if (string.IsNullOrWhiteSpace(commentInputField.text))
        {
            string message = "Comment content cannot be empty";
            Debug.Log(message);
            infoText.text = message;
            infoText.color = Color.red;
            return;
        }
        req.commentContent = commentInputField.text;
        
        DoEditReply(req);
    }

    private void discardReply()
    {
        AddReplyMode();
    }
    
    public void DoEditReply(CommentEditRequest commentEditRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/edit" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{replyId});
        string bodyJsonString = JsonConvert.SerializeObject(commentEditRequest);
        Debug.Log("body json for edit request is "+ bodyJsonString);
        StartCoroutine(PostEdit(url, bodyJsonString));
    }

    /*
    public void EditComment(CommentEditRequest commentEditRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/edit";
        string bodyJsonString = JsonConvert.SerializeObject(commentEditRequest);
        StartCoroutine(Put(url, bodyJsonString));
    }
    */

    // Comment creation is not done at this layer. Replying to comment in this layer is 
    // equivalent to comment creation in the upper layer
    /*
    public void CreateComment(CommentCreateRequest commentCreateRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/create";
        string bodyJsonString = JsonConvert.SerializeObject(commentCreateRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }
    */

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
        HandleResponseReply(request);
        
        request.uploadHandler.Dispose();
        request.downloadHandler.Dispose();
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
        HandleResponseEdit(request);
        
        request.uploadHandler.Dispose();
        request.downloadHandler.Dispose();
    }

    /*
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
        
        request.uploadHandler.Dispose();
        request.downloadHandler.Dispose();
    }
    */

    IEnumerator Delete(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        HandleResponse(request);
        
        request.downloadHandler.Dispose();
    }
    
    private void HandleResponseReply(UnityWebRequest request)
    {
        string response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success: " + response);
            infoText.text = "Success to create reply";
            infoText.color = Color.green;
            commentInputField.text = "";
        }
        else
        {
            Debug.LogError("Error: " + response);
            infoText.text = "Failure to create reply";
            infoText.color = Color.red;
        }
        
    }
    
    private void HandleResponseEdit(UnityWebRequest request)
    {
        string response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success: " + response);
            infoText.text = "Success to edit reply";
            infoText.color = Color.green;
            commentInputField.text = "";
            
            // If successfully edited the command, return to add command mode
            AddReplyMode();
        }
        else
        {
            Debug.LogError("Error: " + response);
            infoText.text = "Failure to edit reply";
            infoText.color = Color.red;
        }
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
    }
}