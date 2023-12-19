using System.Collections;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class AchievementPopUp : MonoBehaviour
{
    
    // public TMP_Text achievementTitle;
    public TMP_Text title;
    public TMP_Text description;
    [SerializeField] private Image achievementImage;
    
    public void Init(AchievementResponse achievementResponse)
    {
        // achievementTitle.text = achievementResponse.title;
        string url = AppVariables.HttpImageUrl + achievementResponse.icon;
        title.text = achievementResponse.title;
        description.text = achievementResponse.description;
        StartCoroutine(LoadImageFromURL(url, achievementImage));
    }
    
    private bool isTitle = false;
    public void ChangeText()
    {
        title.gameObject.SetActive(isTitle);
        description.gameObject.SetActive(!isTitle);
        isTitle = !isTitle;
    }
    
    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {   
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(imageUrl);
        yield return request.SendWebRequest();
        if (request.isNetworkError || request.isHttpError)
        {
            Debug.Log(request.error);
        }
        else
        {
            Texture2D texture2;
            texture2 = ((DownloadHandlerTexture) request.downloadHandler).texture;
            Sprite sprite = Sprite.Create(texture2, new Rect(0, 0, texture2.width, texture2.height), new Vector2(0, 0));
            targetImage.sprite = sprite;
        }
    } 
}
