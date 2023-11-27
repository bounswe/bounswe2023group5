using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class MemberBox : MonoBehaviour
{
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text userName;
    
    private CanvasManager canvasManager;

    private void Awake()
    {
       //  gameDetailsButton.onClick.AddListener(OnClickedGameDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(GroupMember memberInfo)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        // StartCoroutine(LoadImageFromURL(url + gameInfo.gameIcon, gameImage));
        // poster.text = postInfo.poster;
        userName.text = memberInfo.userName;

        // If user image functionality will be added, it can be connected here
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
    
}