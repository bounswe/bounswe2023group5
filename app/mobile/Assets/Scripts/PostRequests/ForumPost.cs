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
    [SerializeField] private Button deletePost;
    [SerializeField] private Button editPost;
    [SerializeField] private GameObject deletePanel;
    [SerializeField] private Button yesDelete;
    [SerializeField] private Button noDelete;
    [SerializeField] private TMP_Text deleteText;
    
    [SerializeField] private ForumPostComments commentManager;
    [SerializeField] private ForumCreatePost editPostManager;
    // [SerializeField] private CommentComments L2commentManager;
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;
    private string postId;
    private string userId;
    private GetPostListResponse postInfoVal;

    private void Awake()
    {
        // commentManager = FindObjectOfType(typeof(openComment)) as openComment;
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        forumPostComments.onClick.AddListener(OnClickedForumPostComments);
        editPost.onClick.AddListener(OnClickedEditPost);
        deletePost.onClick.AddListener(OnClickedDeletePost);
        deletePanel.SetActive(false);
    }

    public void Init(GetPostListResponse postInfo, ForumPostComments commentManagerInfo,
        ForumCreatePost editPostManagerInfo)
    {
        postInfoVal = postInfo;
        commentManager = commentManagerInfo;
        editPostManager = editPostManagerInfo;
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
            userId = postInfo.poster.id;
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
        
        
        deletePanel.gameObject.SetActive(false);

        // User can delete and edit her own posts
        if ( (!String.IsNullOrEmpty(userId)) && (userId == PersistenceManager.id))
        {
            deletePost.gameObject.SetActive(true);
            editPost.gameObject.SetActive(true);
        }
        else
        {
            deletePost.gameObject.SetActive(false);
            editPost.gameObject.SetActive(false);
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

        canvasManager.ShowPostComments();
        commentManager.Init(postId, postInfoVal);
    }

    private void OnClickedEditPost()
    {
        editPostManager.Init(postId, postInfoVal);
        canvasManager.ShowCreateEditPostPage();
        
    }
    
    private void OnClickedDeletePost()
    {
        deletePanel.SetActive(true);
        yesDelete.onClick.AddListener(OnClickedYesDelete);
        noDelete.onClick.AddListener(OnClickedNoDelete);
    }
    
    private void OnClickedYesDelete()
    {
        string url = AppVariables.HttpServerUrl + "/post/delete" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{postId});
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
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to delete forum post detail: " + response);
            deleteText.text = "Post is successfully deleted";
        }
        else
        {
            Debug.Log("Error to delete forum post detail: " + response);
            deleteText.text = "Error in deleting post";
        }
        request.downloadHandler.Dispose();
    }
    
    private void OnClickedNoDelete()
    {
        deletePanel.SetActive(false);
    }
}