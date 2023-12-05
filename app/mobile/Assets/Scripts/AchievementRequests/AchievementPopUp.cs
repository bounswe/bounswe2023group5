using System.Collections;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class AchievementPopUp : MonoBehaviour
{
    
    // public TMP_Text achievementTitle;
    public TMP_Text description;
    [SerializeField] private Image achievementImage;
    
    public void Init(AchievementResponse achievementResponse)
    {
        // achievementTitle.text = achievementResponse.title;
        description.text = achievementResponse.description;
        StartCoroutine(LoadImageFromURL(achievementResponse.icon, achievementImage));
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
