using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class Character : MonoBehaviour
{
    [SerializeField] private Image charImage;
    [SerializeField] private Button charDetailsButton;
    // Character Details will be given in the Init call
    // private CharDetailsPage charDetailsManager;

    private CanvasManager canvasManager;
    private CharacterResponse charInfo;
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        charDetailsButton.onClick.AddListener(OnClickedCharDetails);
    }

    public void Init( CharacterResponse charInfoVal/* , CharDetailsPage charDetailsManagerInfo */)
    {
        // charDetailsManager = charDetailsManagerInfo;
        charInfo = charInfoVal;
        
        // Load the character's image
        StartCoroutine(LoadImageFromURL(AppVariables.HttpImageUrl + charInfo.icon, charImage));
        
    }

    private void OnClickedCharDetails()
    {
        // Initialize and open the Character Details Page
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
