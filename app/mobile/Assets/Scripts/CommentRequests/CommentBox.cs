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
    [SerializeField] private Button commentsButton;
    [SerializeField] private CommentComments commentManager;
    
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;

    private string id;
    private string  post;
    private bool isDeleted;
    private string parentComment; 
    private int voteCount;
    private string commentId;
    private CommentReply[] replies;
    private PostComment PostCommentInfoVal;
    private CommentReply commentInfoVal;

    private void Awake()
    {
        commentsButton.onClick.AddListener(OnClickedCommentsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(PostComment commentInfo, CommentComments commentManagerInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";

        PostCommentInfoVal = commentInfo;
        commentManager = commentManagerInfo;
        
        commentContent.text = PostCommentInfoVal.commentContent;
        commenter.text = PostCommentInfoVal.commenter.username;
        // lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(PostCommentInfoVal.overallVote);
        if (PostCommentInfoVal.commenter == null)
        {
            commenter.text = "(anonymous)";
        }
        else
        {
            commenter.text = PostCommentInfoVal.commenter.username;

        }
        commentId = PostCommentInfoVal.id;
        replies = commentInfo.replies;
        
        Debug.Log("Comment id is "+ commentId);
       
    }
    
    public void Init(CommentReply commentInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";

        commentInfoVal = commentInfo;
        
        commentContent.text = commentInfoVal.commentContent;
        commenter.text = commentInfoVal.commenter.username;
        // lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(commentInfoVal.overallVote);
        
        commentId = commentInfoVal.id;
        // second layer commands do not need comments button
        commentsButton.gameObject.SetActive(false);  
        
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
    
    private void OnClickedCommentsButton()
    {
        // Debug.Log("Game Details Button Clicked");
        // canvasManager.ShowGameDetailsPage(gameID);
        
        canvasManager.ShowCommentComments();
        commentManager.Init(commentId, PostCommentInfoVal);
    }
}