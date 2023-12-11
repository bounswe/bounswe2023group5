using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class ForumPost : MonoBehaviour
{
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text poster;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text userName;

    [SerializeField] private Button forumPostComments;
    [SerializeField] private ForumPostComments commentManager;
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;
    private string postId;
    private GetPostListResponse postInfoVal;

    private void Awake()
    {
        // commentManager = FindObjectOfType(typeof(openComment)) as openComment;
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        forumPostComments.onClick.AddListener(OnClickedForumPostComments);
    }

    public void Init(GetPostListResponse postInfo, ForumPostComments commentManagerInfo)
    {
        postInfoVal = postInfo;
        commentManager = commentManagerInfo;
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        // StartCoroutine(LoadImageFromURL(url + gameInfo.gameIcon, gameImage));
        // poster.text = postInfo.poster;
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
        postId = postInfo.id;
        
        Debug.Log("Post id is "+ postId);

        tags.text = "";
        foreach (var tag in postInfo.tags)
        {
            tags.text =  tags.text + tag + " ";
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
    
    private void OnClickedForumPostComments()
    {
        // Debug.Log("Game Details Button Clicked");
        // canvasManager.ShowGameDetailsPage(gameID);
        //GameObject postComments = GameObject.Find("PostComments");
        //postComments.SetActive(true);

        canvasManager.ShowPostComments();
        commentManager.Init(postId);
    }
}