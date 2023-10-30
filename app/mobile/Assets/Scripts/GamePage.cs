using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class GamePage : MonoBehaviour
{
    [SerializeField] private Image gameImage;
    [SerializeField] private TMP_Text gameNameText;
    [SerializeField] private TMP_Text gameDescriptionText;
    
    public void Init(GameInfo gameInfo)
    {
        StartCoroutine(LoadImageFromURL(gameInfo.gameIcon, gameImage));
        gameNameText.text = gameInfo.gameName;
        gameDescriptionText.text = gameInfo.gameDescription;
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