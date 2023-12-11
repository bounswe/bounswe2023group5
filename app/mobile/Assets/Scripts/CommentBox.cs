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
    // [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;

    private string id;
    private string  post;
    private bool isDeleted;
    private string parentComment; 
    private int voteCount;

    private void Awake()
    {
       //  gameDetailsButton.onClick.AddListener(OnClickedGameDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(PostComment postInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        
        commentContent.text = postInfo.commentContent;
        commenter.text = postInfo.commenter.username;
        // lastEditedAt.text = postInfo.lastEditedAt;
        overallVote.text = Convert.ToString(postInfo.overallVote);

       
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