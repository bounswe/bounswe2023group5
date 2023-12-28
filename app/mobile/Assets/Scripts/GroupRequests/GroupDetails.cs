using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DG.Tweening;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.Serialization;
using UnityEngine.UI;
using Utilities;

public class GroupDetails : MonoBehaviour
{

    [SerializeField] private Button descriptionButton;
    [SerializeField] private Button forumButton;
    [SerializeField] private Button membersButton;
    [SerializeField] private GameObject descriptionManager;
    [SerializeField] private GetGroupMemberList getAllMembers;

    [SerializeField] private ForumGetPostList forumManager;
    [SerializeField] private Button addForumPost;
    [SerializeField] private ForumCreatePost forumCreatePostManager;

    [SerializeField] private Button exitButton;
    
    string pictureURL = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
    
    private CanvasManager canvasManager;
    // Colors will be used to retrieve the colors of the color palette of the project
    private Colors colors;
    
    
    // Group image will be game image
    [SerializeField] private Image gameImage;
    [SerializeField] private TMP_Text headingText;
    [SerializeField] private TMP_Text bottomText;
    
    // Group icon will be game icon
    private string gameIcon;
    
    private string groupId; // id
    private string title;
    private string description;
    private string membershipPolicy;
    private TagResponse[] tags;
    private string gameId;
    private string forumId;
    private int quota;
    private GroupMember[] moderators;
    private GroupMember[] members;
    private GroupMember[] bannedMembers;
    private bool avatarOnly;
    private bool userJoined;
    private string createdAt;
    

    
    private void Awake()
    {
        descriptionButton.onClick.AddListener(OnClickedDescriptionButton);
        forumButton.onClick.AddListener(OnClickedForumButton);
        membersButton.onClick.AddListener(OnClickedMembersButton);
        exitButton.onClick.AddListener(OnClickedExitButton);
        addForumPost.onClick.AddListener(OnClickedAddPost);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;

        colors = new Colors();
        
        // Init();
    }
    
    /*
    private void Start()
    {
        descriptionButton.onClick.AddListener(OnClickedDescriptionButton);
        forumButton.onClick.AddListener(OnClickedForumButton);
        membersButton.onClick.AddListener(OnClickedMembersButton);
        exitButton.onClick.AddListener(OnClickedExitButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;

        colors = new Colors();
    }
    */


    public void Init(string _groupId)
    {
        groupId = _groupId;
        // groupId = "8e418111-3445-4ea2-9567-aeb0d26d5a79";
        OnClickedDescriptionButton();
        GetGroupDescription();
    }

    public void ShowForumManager(bool b)
    {
        
        forumManager.gameObject.SetActive(b);

        // If it is a public group or the user is a member, she can post forum posts
        if (membershipPolicy == "PUBLIC" ||
            (members != null && 
             members.Select(member => member.id).ToArray().Contains(PersistenceManager.id)))
        {
            addForumPost.gameObject.SetActive(b);
        }
        else
        {
            addForumPost.gameObject.SetActive(false);
        }
    }
    
    
    private void OnClickedExitButton()
    {
        canvasManager.HideGroupDetailsPage();
    }
    
    private void OnClickedDescriptionButton()
    {
        descriptionButton.image.color = colors.blueGreen;
        forumButton.image.color = colors.prussianBlueLight;
        membersButton.image.color = colors.prussianBlueLight;
        
        descriptionManager.gameObject.SetActive(true);
        ShowForumManager(false);
        getAllMembers.gameObject.SetActive(false);
    }
    
    public void OnClickedForumButton()
    {
        if (String.IsNullOrEmpty(forumId))
        {
            return;
        }
        descriptionButton.image.color = colors.prussianBlueLight;
        forumButton.image.color = colors.blueGreen;
        membersButton.image.color = colors.prussianBlueLight;
        
        descriptionManager.gameObject.SetActive(false);
        ShowForumManager(true);
        getAllMembers.gameObject.SetActive(false);
        
        // 3 parameters are required // Will be changed
        forumManager.ListForumPosts(new [] {"forum", "sortBy", "sortDirection"},
            new [] {forumId, "CREATION_DATE", "ASCENDING"}, forumCreatePostManager);
    }
    
    private void OnClickedMembersButton()
    {
        descriptionButton.image.color = colors.prussianBlueLight;
        forumButton.image.color = colors.prussianBlueLight;
        membersButton.image.color = colors.blueGreen;
        
        descriptionManager.gameObject.SetActive(false);
        ShowForumManager(false);
        getAllMembers.gameObject.SetActive(true);
        
        getAllMembers.GetMemberList(moderators, members, bannedMembers, PersistenceManager.id, groupId);
        //Debug.Log("Returned from GetMemberList");
    }
    
    private void OnClickedAddPost()
    {
        canvasManager.ShowCreateEditPostPage();
        forumCreatePostManager.Init(forumId, gameId);
    }
    
    

    private void GetGroupDescription()
    {
        // Make a request to game id
        string url = AppVariables.HttpServerUrl + "/group/get" + 
                     ListToQueryParameters.ListToQueryParams(
                         new []{"id"}, new []{groupId});

        StartCoroutine(Get(url));
    }

    IEnumerator Get(string url)
    {
        // var parameteredUrl = url + "?gameId=" + gameId;
        
        // Debug.Log(url);
        
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        

        yield return request.SendWebRequest();

        var response = request.downloadHandler.text;
        Debug.Log(response);
        

        if (request.responseCode != 200 )
        {
            bottomText.text = "Error: " + request.responseCode;
            bottomText.color = Color.red;
        }
        else
        {
            GroupResponse _groupData;

            try
            {
                _groupData = JsonConvert.DeserializeObject<GroupResponse>(response);
            }
            catch (JsonReaderException e)
            {
                Debug.Log("JsonReaderException: " + e);
                throw e;
            }
            catch (JsonSerializationException e)
            {
                Debug.Log("JsonSerializationException: " + e);
                throw e;
            }
        

            // Set all the variables corresponding to fields
            
    
            groupId = _groupData.id; // id
            title = _groupData.title;
            description = _groupData.description;
            membershipPolicy = _groupData.membershipPolicy;
            tags = _groupData.tags;
            gameId = _groupData.gameId;
            forumId = _groupData.forumId;
            quota = _groupData.quota;
            moderators = _groupData.moderators;
            members = _groupData.members;
            Debug.Log("members: "+ members);
            bannedMembers = _groupData.bannedMembers;
            avatarOnly = _groupData.avatarOnly;
            createdAt = _groupData.createdAt;
            userJoined = _groupData.userJoined;

            // Set the image via call to GetGame
            StartCoroutine(GetGameImage(gameId));
            
            
            // Set the bottom text
            bottomText.text = description;
            Debug.Log("After bottom text is set");
            
            // Set heading
            headingText.text = title;
            Debug.Log("After heading text is set");
        }
        request.downloadHandler.Dispose();
       
    }
    
    
    IEnumerator GetGameImage(string gameId)
    {
        string url = AppVariables.HttpServerUrl + "/game/get-game" +
                     ListToQueryParameters.ListToQueryParams(new[] { "gameId" },
                         new[] { gameId });
        
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");

        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            var _GetGameResponseData = JsonConvert.DeserializeObject<GameDetail>(response);

            if (!_GetGameResponseData.gameIcon.Contains("webp"))
            {
                StartCoroutine(LoadImageFromURL(pictureURL +
                                                _GetGameResponseData.gameIcon, gameImage));
            }

            Debug.Log("Success to get game: " + response);
        }
        else
        {
            Debug.Log("Error to get game: " + response);
        }
        request.downloadHandler.Dispose();
    }

    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {
        if (imageUrl.Contains("webp"))
        {
            yield return null;
        }
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
    
    
}
