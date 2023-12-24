using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class CommentBox : MonoBehaviour
{
    // [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text commenter;
    [SerializeField] private TMP_Text commentContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private Button upvoteButton;
    [SerializeField] private Button downvoteButton;
    //[SerializeField] private Button commentsButton;
    [SerializeField] private Button repliesButton;
    [SerializeField] private Button editButton;
    [SerializeField] private Button deleteButton;
    [SerializeField] private GameObject deletePanel;
    [SerializeField] private Button yesDelete;
    [SerializeField] private Button noDelete;
    [SerializeField] private TMP_Text deleteText;
    
    [SerializeField] private CommentComments commentManager;
    [SerializeField] private ForumPostComments editCommentManager;
    [SerializeField] private CommentComments editReplyManager;
    
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;

    private string userId; // user id
    private string commentId;
    private string  post;
    private bool isDeleted;
    private string parentComment; 
    private int voteCount;
    private CommentReply[] replies;
    private PostComment PostCommentInfoVal;
    private CommentReply commentInfoVal;

    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        
        
    }

    public void Init(PostComment commentInfo, CommentComments commentManagerInfo, 
        ForumPostComments editCommentManagerInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";

        PostCommentInfoVal = commentInfo;
        commentManager = commentManagerInfo;
        editCommentManager = editCommentManagerInfo;
        
        // Add listeners to the buttons
        //commentsButton.onClick.AddListener(OnClickedCommentsButton);
        repliesButton.onClick.AddListener(OnClickedRepliesButton);
        editButton.onClick.AddListener(OnClickedEditButton);
        deleteButton.onClick.AddListener(OnClickedDeleteButton);
        deletePanel.SetActive(false);
        
        commentContent.text = PostCommentInfoVal.commentContent;
        // lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(PostCommentInfoVal.overallVote);
        if (PostCommentInfoVal.commenter == null)
        {
            commenter.text = "(anonymous)";
        }
        else
        {
            commenter.text = PostCommentInfoVal.commenter.username;
            userId = PostCommentInfoVal.commenter.id;

        }
        commentId = PostCommentInfoVal.id;
        replies = commentInfo.replies;
        
        
        deletePanel.gameObject.SetActive(false);
        
        // User can delete and edit her own posts
        if ( (!String.IsNullOrEmpty(userId)) && (userId == PersistenceManager.id))
        {
            deleteButton.gameObject.SetActive(true);
            editButton.gameObject.SetActive(true);
        }
        else
        {
            deleteButton.gameObject.SetActive(false);
            editButton.gameObject.SetActive(false);
        }
        
        Debug.Log("Comment id is "+ commentId);
       
    }
    
    public void Init(CommentReply commentInfo, CommentComments editReplyManagerInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        
        editReplyManager = editReplyManagerInfo;
        commentInfoVal = commentInfo;
        
        // Add listeners to the buttons
        editButton.onClick.AddListener(OnClickedEditReplyButton);
        deleteButton.onClick.AddListener(OnClickedDeleteButton);
        deletePanel.SetActive(false);
        
        commentContent.text = commentInfoVal.commentContent;
        // lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(commentInfoVal.overallVote);
        if (commentInfoVal.commenter == null)
        {
            commenter.text = "(anonymous)";
        }
        else
        {
            commenter.text = commentInfoVal.commenter.username;
            userId = commentInfoVal.commenter.id;

        }
        
        commentId = commentInfoVal.id;
        // second layer commands do not need comments button
        // commentsButton.gameObject.SetActive(false);  
        repliesButton.gameObject.SetActive(false);  
        
        // User can delete and edit her own posts
        if ( (!String.IsNullOrEmpty(userId)) && (userId == PersistenceManager.id))
        {
            deleteButton.gameObject.SetActive(true);
            editButton.gameObject.SetActive(true);
        }
        else
        {
            deleteButton.gameObject.SetActive(false);
            editButton.gameObject.SetActive(false);
        }
        
        Debug.Log("Comment id is "+ commentId);
       
    }

    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(imageUrl);
        yield return request.SendWebRequest();

        if(request.result != UnityWebRequest.Result.Success)
        {
            // Debug.LogError("Failed to load image: " + request.error);
        }
        else
        {
            Texture2D texture = DownloadHandlerTexture.GetContent(request);
            targetImage.sprite = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }
    }
    
    private void OnClickedRepliesButton()
    {
        // Debug.Log("Game Details Button Clicked");
        // canvasManager.ShowGameDetailsPage(gameID);
        
        canvasManager.ShowCommentComments();
        commentManager.Init(commentId, PostCommentInfoVal);
    }
    
    // Here we will use forumPostComments, which is already open and comment is part of this page
    private void OnClickedEditButton()
    {
        // editCommentManager.Init(postId, postInfoVal);
        // canvasManager.ShowCreateEditPostPage();
        editCommentManager.EditCommentMode(PostCommentInfoVal);
        
    }
    
    private void OnClickedEditReplyButton()
    {
        // editCommentManager.Init(postId, postInfoVal);
        // canvasManager.ShowCreateEditPostPage();
        editReplyManager.EditReplyMode(commentInfoVal);
        
    }
    
    private void OnClickedDeleteButton()
    {
        deletePanel.SetActive(true);
        yesDelete.onClick.AddListener(OnClickedYesDelete);
        noDelete.onClick.AddListener(OnClickedNoDelete);
    }
    
    private void OnClickedYesDelete()
    {
        string url = AppVariables.HttpServerUrl + "/comment/delete" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{commentId});
        StartCoroutine(Delete(url));
    }
    
    IEnumerator Delete(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success to delete comment detail: " + response);
            deleteText.text = "Comment is successfully deleted";
        }
        else
        {
            Debug.Log("Error to delete comment detail: " + response);
            deleteText.text = "Error in deleting comment";
        }
        request.downloadHandler.Dispose();
    }
    
    private void OnClickedNoDelete()
    {
        deletePanel.SetActive(false);
    }
}