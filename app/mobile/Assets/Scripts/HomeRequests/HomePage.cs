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
    [SerializeField] private Button upVoteButton;
    [SerializeField] private Button downVoteButton;
    private CanvasManager canvasManager;
    
    private string typeId;
    private string voteType;
    private string id;
    private int overallVote;
    private int choosenVote;

    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        upVoteButton.onClick.AddListener(OnClickedUpVote);
        downVoteButton.onClick.AddListener(OnClickedDownVote);
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
        typeId = homeInfo.typeId;
        voteType = homeInfo.type;
        id = homeInfo.id;
        overallVote = homeInfo.overallVote;
        
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
        achievementCreateRequest.voteType = voteType;
        achievementCreateRequest.typeId = typeId;
        achievementCreateRequest.choice = "UPVOTE";
        string bodyJsonString = JsonUtility.ToJson(achievementCreateRequest);
        StartCoroutine(PostVote(bodyJsonString));
    }
    
    public void OnClickedDownVote()
    {
        var achievementCreateRequest = new CreateVoteRequest();
        achievementCreateRequest.voteType = voteType;
        achievementCreateRequest.typeId = typeId;
        achievementCreateRequest.choice = "DOWNVOTE";
        string bodyJsonString = JsonUtility.ToJson(achievementCreateRequest);
        StartCoroutine(PostVote(bodyJsonString));
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
        }
        else
        {
            Debug.Log("Error to create vote: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}