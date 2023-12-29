using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class HomePage : MonoBehaviour
{
    [SerializeField] private Image homeImage;
    [SerializeField] private TMP_Text homeTitleText;
    [SerializeField] private TMP_Text homeContentText;
    [SerializeField] private TMP_Text dateText;
    [SerializeField] private TMP_Text voteText;
    [SerializeField] private TMP_Text typeNameText;
    [SerializeField] private TMP_Text userNameText;
    [SerializeField] private Button upVoteButton;
    [SerializeField] private Button downVoteButton;
    [SerializeField] private Button typeButton;
    [SerializeField] private Button readMoreButton;
    [SerializeField] private Button deleteButton;
    [SerializeField] private Button hideButton;
    
    private CanvasManager canvasManager;
    
    private string typeId;
    private string voteType;
    private string id;
    private int overallVote;
    private int choosenVote;
    private string type;

    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        upVoteButton.onClick.AddListener(OnClickedUpVote);
        downVoteButton.onClick.AddListener(OnClickedDownVote);
        typeButton.onClick.AddListener(OnClickedTypeButton);
        readMoreButton.onClick.AddListener(OnClickedReadMoreButton);
        deleteButton.onClick.AddListener(OnClickedDeleteButton);
        hideButton.onClick.AddListener(OnClickedHideButton);
        if (PersistenceManager.Role == "ADMIN")
        {
            deleteButton.gameObject.SetActive(true);
        }
        else
        {
            deleteButton.gameObject.SetActive(false);
        }
    }

    public void Init(HomeResponse homeInfo)
    {
        string url = AppVariables.HttpImageUrl;
        if (homeInfo.postImage != null)
        {
            StartCoroutine(LoadImageFromURL(url + homeInfo.postImage, homeImage));
        }
        homeTitleText.text = homeInfo.title;
        homeContentText.text = homeInfo.postContent;
        dateText.text = homeInfo.lastEditedAt.ToString("dd/MM/yyyy");
        voteText.text = homeInfo.overallVote.ToString();
        typeNameText.text = homeInfo.typeName;
        type = homeInfo.type;
        typeId = homeInfo.typeId;
        voteType = homeInfo.type;
        id = homeInfo.id;
        overallVote = homeInfo.overallVote;
        userNameText.text = homeInfo.poster?.username;

        upVoteButton.interactable = homeInfo.userVote != "UPVOTE";
        downVoteButton.interactable = homeInfo.userVote != "DOWNVOTE";
        
        

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
            targetImage.gameObject.SetActive(true);
            targetImage.sprite = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }
    }
    
    public void OnClickedUpVote()
    {
        var achievementCreateRequest = new CreateVoteRequest();
        achievementCreateRequest.voteType = "POST";
        achievementCreateRequest.typeId = id;
        achievementCreateRequest.choice = "UPVOTE";
        string bodyJsonString = JsonUtility.ToJson(achievementCreateRequest);
        StartCoroutine(PostVote(bodyJsonString));
    }
    
    public void OnClickedDownVote()
    {
        var achievementCreateRequest = new CreateVoteRequest();
        achievementCreateRequest.voteType = "POST";
        achievementCreateRequest.typeId = id;
        achievementCreateRequest.choice = "DOWNVOTE";
        string bodyJsonString = JsonUtility.ToJson(achievementCreateRequest);
        StartCoroutine(PostVote(bodyJsonString));
    }
    
    public void OnClickedTypeButton()
    {
        switch (type)
        {
            case "GAME":
                canvasManager.ShowGameDetailsPage(typeId);
                break;
            case "GROUP":
                canvasManager.ShowGroupDetailsPage(typeId);
                break;
        }
    }
    
    private void OnClickedReadMoreButton()
    {
        canvasManager.ShowPostComments(id);
    }
    
    private void OnClickedDeleteButton()
    {
        StartCoroutine(DeletePost(AppVariables.HttpServerUrl + "/post/delete" + "?id=" + id));
    }
    
    private void OnClickedHideButton()
    {
        Destroy(gameObject);
    }
    
    IEnumerator DeletePost(string url)
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
            Debug.Log("Success to delete post: " + response);
            Destroy(gameObject);
        }
        else
        {
            Debug.Log("Error to delete post: " + response);
        }
        request.downloadHandler.Dispose();
    }

    IEnumerator PostVote(string bodyJsonString)
    {
        string url = AppVariables.HttpServerUrl + "/vote/create";
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
            var voteResponse = JsonConvert.DeserializeObject<VoteResponse>(response);
            voteText.text = voteResponse.choice == "UPVOTE" ? overallVote + 1 + "" : overallVote - 1 + "";
            upVoteButton.interactable = voteResponse.choice == "UPVOTE" ? false : true;
            downVoteButton.interactable = voteResponse.choice == "DOWNVOTE" ? false : true;
            Debug.Log("Success to create vote: " + response);
            // StartCoroutine(GetVote(AppVariables.HttpServerUrl + "/vote/get" + "?id=" + voteResponse.id));
        }
        else
        {
            Debug.Log("Error to create vote: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    IEnumerator GetVote(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get vote: " + response);
        }
        else
        {
            Debug.Log("Error to get vote: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}