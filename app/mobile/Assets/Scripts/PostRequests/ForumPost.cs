using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;
using UnityEngine.Serialization;

public class ForumPost : MonoBehaviour
{
    [SerializeField] private Image postImage;
    [SerializeField] private TMP_Text poster;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private ScrollRect tagScroll;
    [SerializeField] private Transform tagPageParent;
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
    private List<Tag> tagObjects = new List<Tag>();

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
        if (postInfo.postImage != null)
        {
            StartCoroutine(LoadImageFromURL(AppVariables.HttpImageUrl + postInfo.postImage, postImage));
        }
        else
        {
            postImage.gameObject.SetActive(false);
        }

        // poster.text = postInfo.poster;
        title.text = postInfo.title;
        postContent.text = postInfo.postContent;
        lastEditedAt.text = postInfo.lastEditedAt.ToString("dd/MM/yyyy");
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
        
        Debug.Log("Post id is "+ postId);

        AddTags(postInfo.tags);
        
        if (postInfo.isEdited)
        {
            // lastEditedAt.text += " (edited)";
        }
        else
        {
            // This will be deleted
            // lastEditedAt.text += " (not edited)";
        }
        
        
        deletePanel.gameObject.SetActive(false);

        // User can delete and edit her own posts
        if ( (!String.IsNullOrEmpty(userId)) && (userId == PersistenceManager.id) || (PersistenceManager.Role == "ADMIN"))
        {
            deletePost.gameObject.SetActive(true);
            editPost.gameObject.SetActive(true);
        }
        else
        {
            deletePost.gameObject.SetActive(false);
            editPost.gameObject.SetActive(false);
        }
        
        // Admin can delete any post
        if ( PersistenceManager.Role == "ADMIN")
        {
            deletePost.gameObject.SetActive(true);
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
            targetImage.gameObject.SetActive(true);
        }
    }
    
    private void OnClickedForumPostComments()
    {
        canvasManager.ShowPostComments(postId);
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