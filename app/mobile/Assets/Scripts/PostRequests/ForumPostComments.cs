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

public class ForumPostComments : MonoBehaviour
{
    //[SerializeField] private string title;
    //[SerializeField] private string postContent;
    //[SerializeField] private ForumPostDetail forumPost;
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform commentParent;
    private List<CommentBox> commentPages = new List<CommentBox>();
    [SerializeField] private string postID;
    private CanvasManager canvasManager;
    [SerializeField] private TMP_InputField commentInputField;
    [SerializeField] private Button addCommentButton;
    [SerializeField] private Button editCommentButton;
    [SerializeField] private Button discardCommentButton;
    [SerializeField] private Button exitButton;
    [SerializeField] private TMP_Text infoText;
    private GetPostListResponse postInfo;
    [SerializeField] private Image userImage;
    
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private ScrollRect tagScroll;
    [SerializeField] private Transform tagPageParent;
    [SerializeField] private TMP_Text userName;
    [SerializeField] private Character character;
    [SerializeField] private ScrollRect achievementScroll;

    [SerializeField] private CommentComments L2commentManager;
    [SerializeField] private CharacterDetails characterDetailsManager;

    

    // will be used in comment edit mode
    private string commentId;
    private List<Tag> tagObjects = new List<Tag>();
    
    private void Awake()
    {
        addCommentButton.onClick.AddListener(addComment);
        editCommentButton.onClick.AddListener(editComment);
        discardCommentButton.onClick.AddListener(discardComment);
        exitButton.onClick.AddListener(exit);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(string id)
    {
        // Page is in add comment mode
        AddCommentMode();
        
        postID = id;
        string detailInfoURL = AppVariables.HttpServerUrl + "/post/get-post-detail" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{postID});
        StartCoroutine(GetDetailedInfo(detailInfoURL));
        
        infoText.text = "";
        // this.L2commentManager = L2commentManagerInfo;

        if (L2commentManager == null)
        {
            Debug.Log("l2 comment manager is null");
        }
        else
        {
            Debug.Log("l2 comment manager is not null");

        }
        

    }
    
    private void AddTags(TagResponse[] tags)
    {
        foreach (var tagObj in tagObjects)
        {
            Destroy(tagObj.gameObject);
        }
        tagObjects.Clear();

        foreach (var tag in tags)
        {
            Tag tagObj = Instantiate(Resources.Load<Tag>("Prefabs/Tag"), tagPageParent);
            tagObjects.Add(tagObj);
            tagObj.Init(tag);
        }
        Canvas.ForceUpdateCanvases();
        tagScroll.horizontalNormalizedPosition = 1;
        Debug.Log("Success to list tags");
    }
    
    IEnumerator GetDetailedInfo(string _url)
    {

        var request = new UnityWebRequest(_url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            
            var _postData = JsonConvert.DeserializeObject<GetPostListResponse>(response);
            
            postInfo = _postData;
            
            // forumPost.Init(postID);
            title.text = postInfo.title;
            postContent.text = postInfo.postContent;
            lastEditedAt.text = postInfo.lastEditedAt;
            overallVote.text = Convert.ToString(postInfo.overallVote);
            if (postInfo.poster == null)
            {
                userName.text = "(anonymous)";
            }
            else
            {
                userName.text = postInfo.poster.username;
            }

            AddTags(postInfo.tags);
            character.gameObject.SetActive(false);
            if (postInfo.character != null)
            {
                character.gameObject.SetActive(true);
                character.Init(postInfo.character, characterDetailsManager);
            }

            if (postInfo.isEdited)
            {
                lastEditedAt.text += " (edited)";
            }
            else
            {
                // This will be deleted
                lastEditedAt.text += " (not edited)";
            }
        
            //GameObject postComments = GameObject.Find("PostComments");
            //postComments.SetActive(true);

        
            if (string.IsNullOrEmpty(postID))
            {
                Debug.Log("Id must be specified");
            }
        
            string url = AppVariables.HttpServerUrl + "/post/get-post-comments" + 
                         ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{postID});
            StartCoroutine(Get(url));
            
            Debug.Log("Success to get forum post data: " + response);
        }
        else
        {
            Debug.Log("Error to get forum post data: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
    IEnumerator Get(string url)
    {
        foreach (var commentPage in commentPages)
        {
            Destroy(commentPage.gameObject);
        }
        commentPages.Clear();
        
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            
            var _gamesData = JsonConvert.DeserializeObject<PostComment[]>(response);
            
            foreach (var comment in _gamesData)
            {
                if (comment.isDeleted)
                {
                    Debug.Log("deleted comment reply is:\n" + comment);
                    continue;
                }
                Debug.Log("comment data is:\n" + comment);
                CommentBox newComment = Instantiate(Resources.Load<CommentBox>("Prefabs/CommentPage"), commentParent);
                commentPages.Add(newComment);
                newComment.Init(comment, L2commentManager, this);
            }
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
            
            Debug.Log("Success to get forum post comments: " + response);
        }
        else
        {
            Debug.Log("Error to get forum post comments: " + response);
        }
        request.downloadHandler.Dispose();
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
        req.post = postID;
        
        DoCreateComment(req);
    }

    private void exit()
    {
        canvasManager.HidePostComments();
    }
    
    
    
    public void ReplyToComment(CommentReplyRequest commentReplyRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/reply";
        string bodyJsonString = JsonConvert.SerializeObject(commentReplyRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }

    private void AddCommentMode()
    {
        commentInputField.text = "";
        commentId = "";
        
        // open the add comment button and close the edit comment button
        addCommentButton.gameObject.SetActive(true);
        editCommentButton.gameObject.SetActive(false);
        discardCommentButton.gameObject.SetActive(true);
    }
    public void EditCommentMode(PostComment postCommentInfo)
    {
        commentInputField.text = postCommentInfo.commentContent;
        commentId = postCommentInfo.id;
        
        // close the add comment button and open the edit comment button
        addCommentButton.gameObject.SetActive(false);
        editCommentButton.gameObject.SetActive(true);
        discardCommentButton.gameObject.SetActive(true);
    }

    private void editComment()
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
        
        DoEditComment(req);
    }
    
    private void discardComment()
    {
        AddCommentMode();
    }
    
    public void DoEditComment(CommentEditRequest commentEditRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/edit" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{commentId});
        string bodyJsonString = JsonConvert.SerializeObject(commentEditRequest);
        Debug.Log("body json for edit request is "+ bodyJsonString);
        StartCoroutine(PostEdit(url, bodyJsonString));
    }

    public void DoCreateComment(CommentCreateRequest commentCreateRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/create";
        string bodyJsonString = JsonConvert.SerializeObject(commentCreateRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }

    public void DoDeleteComment(string commentId)
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
        HandleResponseCreate(request);
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
        HandleResponseEdit(request);
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
    
    private void HandleResponseCreate(UnityWebRequest request)
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
            infoText.text = "Failure to create comment";
            infoText.color = Color.red;
        }
        request.downloadHandler.Dispose();
    }
    
    private void HandleResponseEdit(UnityWebRequest request)
    {
        string response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success: " + response);
            infoText.text = "Success to edit comment";
            infoText.color = Color.green;
            commentInputField.text = "";
            
            // If successfully edited the command, return to add command mode
            AddCommentMode();
        }
        else
        {
            Debug.LogError("Error: " + response);
            infoText.text = "Failure to edit comment";
            infoText.color = Color.red;
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