using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;
using Utilities;

public class ForumPost : MonoBehaviour
{
    private string postId;
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text poster;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text userName;
    [SerializeField] private Button upVote;
    [SerializeField] private Button downVote;
    [SerializeField] private ScrollRect textScroll;
    [SerializeField] private ScrollRect tagScroll;
    [SerializeField] private Transform tagPageParent;
    private List<TagBox2> tagPages = new List<TagBox2>();
    
    //[SerializeField] private CreateVote createVote;
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;

    
    // Colors will be used to retrieve the colors of the color palette of the project
    private Colors colors;

    private void Awake()
    {
       //  gameDetailsButton.onClick.AddListener(OnClickedGameDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        colors = new Colors();
        userImage.color = colors.UTOrange;
        
        upVote.onClick.AddListener(doUpVote);
        downVote.onClick.AddListener(doDownVote);
    }

   

    public void Init(GetPostListResponse postInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        // StartCoroutine(LoadImageFromURL(url + gameInfo.gameIcon, gameImage));
        // poster.text = postInfo.poster;
        postId = postInfo.id;
        title.text = postInfo.title;
        postContent.text = postInfo.postContent;
        lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(postInfo.overallVote);
        if (postInfo.poster == null)
        {
            userName.text = "Anonymous";
        }
        else
        {
            userName.text = postInfo.poster.username;
        }
        

        
        tags.text = "";
        foreach (var tag in postInfo.tags)
        {
            TagBox2 newTagBox = Instantiate(Resources.Load<TagBox2>("Prefabs/TagBox"), tagPageParent);
            tagPages.Add(newTagBox);
            newTagBox.Init(tag);
        }
        Canvas.ForceUpdateCanvases();
        tagScroll.horizontalNormalizedPosition = 1;
        
        
        if (postInfo.isEdited)
        {
            lastEditedAt.text += " (edited)";
        }
        else
        {
            // This will be deleted
            lastEditedAt.text += " (not edited)";
        }
        
        Canvas.ForceUpdateCanvases();
        textScroll.verticalNormalizedPosition = 1;
        tagScroll.horizontalNormalizedPosition = 1;
    }
    
    private void doUpVote()
    {
        doCreateVote("POST", postId, "UPVOTE");
        
    }
    
    private void doDownVote()
    {
        doCreateVote("POST", postId, "DOWNVOTE");
        
    }
    
    public void doCreateVote(string voteType, string typeId, string choice)
    {
        string url = AppVariables.HttpServerUrl + "/vote/create";
        var achievementCreateRequest = new CreateVoteRequest();
        achievementCreateRequest.voteType = voteType;
        achievementCreateRequest.typeId = typeId;
        achievementCreateRequest.choice = choice;
        string bodyJsonString = JsonUtility.ToJson(achievementCreateRequest);
        StartCoroutine(PostVote(url, bodyJsonString));
    }
    IEnumerator PostVote(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to create vote: " + response);
        }
        else
        {
            Debug.Log("Error to create vote: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
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
    
    private void OnClickedGameDetailsButton()
    {
        // Debug.Log("Game Details Button Clicked");
        // canvasManager.ShowGameDetailsPage(gameID);
    }
}