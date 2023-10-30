using System;
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
    [SerializeField] private Button gameDetailsButton;
    private CanvasManager canvasManager;
    private string gameID;

    private void Awake()
    {
        gameDetailsButton.onClick.AddListener(OnClickedGameDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(GameListEntry gameInfo)
    {
        // StartCoroutine(LoadImageFromURL(gameInfo.gameIcon, gameImage));
        gameNameText.text = gameInfo.gameName;
        gameDescriptionText.text = gameInfo.gameDescription;
        gameID = gameInfo.id;
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
        canvasManager.ShowGameDetailsPage(gameID);
    }
}