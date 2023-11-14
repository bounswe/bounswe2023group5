using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class ForumPage : MonoBehaviour
{
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text poster;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text userName;
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;

    private void Awake()
    {
       //  gameDetailsButton.onClick.AddListener(OnClickedGameDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(PostListEntry postInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        // StartCoroutine(LoadImageFromURL(url + gameInfo.gameIcon, gameImage));
        // poster.text = postInfo.poster;
        title.text = postInfo.title;
        postContent.text = postInfo.postContent;
        lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(postInfo.overallVote);
        userName.text = postInfo.poster.username;

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
    
    private void OnClickedGameDetailsButton()
    {
        // Debug.Log("Game Details Button Clicked");
        // canvasManager.ShowGameDetailsPage(gameID);
    }
}