using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class HomePage : MonoBehaviour
{
    [SerializeField] private Image homeImage;
    [SerializeField] private TMP_Text homeTitleText;
    [SerializeField] private TMP_Text homeContentText;
    private CanvasManager canvasManager;

    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
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
}