using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class Character : MonoBehaviour
{
    [SerializeField] private Image backImage;
    [SerializeField] private Image charImage;
    [SerializeField] private Button charDetailsButton;
    [SerializeField] private Button charAddButton;

    [SerializeField] private CharacterDetails characterDetailsManager;
    // Character Details will be given in the Init call
    // private CharDetailsPage charDetailsManager;

    private CanvasManager canvasManager;
    private CharacterResponse charInfo;

    private string pageMode;
    private bool charAdded; // will be used in character addition mode
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        charDetailsButton.onClick.AddListener(OnClickedCharDetails);
        charAddButton.onClick.AddListener(OnClickedCharAdd);
    }

    // Called by characters page, character details mode
    public void Init( CharacterResponse charInfoVal, CharacterDetails charDetailsManagerInfo )
    {
        characterDetailsManager = charDetailsManagerInfo;
        charInfo = charInfoVal;
        
        PageMode("details");

        if (characterDetailsManager == null)
        {
            Debug.Log("Character details manager is null");
        }
        else
        {
            Debug.Log("Character details manager is not null");
        }

        if (!charInfo.icon.Contains("webp"))
        {
            // Load the character's image
            StartCoroutine(LoadImageFromURL(AppVariables.HttpImageUrl + charInfo.icon, charImage));
        }
    }
    
    // Called by forum create post, character addition mode
    public void Init( CharacterResponse charInfoVal)
    {
        charInfo = charInfoVal;
        
        PageMode("add");

        if (!charInfo.icon.Contains("webp"))
        {
            // Load the character's image
            StartCoroutine(LoadImageFromURL(AppVariables.HttpImageUrl + charInfo.icon, charImage));
        }
    }

    private void OnClickedCharDetails()
    {
        
        // Initialize and open the Character Details Page
        canvasManager.ShowCharacterDetailsPage();
        characterDetailsManager.Init(charInfo);
    }
    
    private void OnClickedCharAdd()
    {
        if (charAdded)
        {
            // Remove the selection color
            Color currentColor = backImage.color;
            currentColor.a = 0.0f;
            backImage.color = currentColor;
            
            charAdded = false;
        }
        else
        {
            // Add the selection color
            Color currentColor = backImage.color;
            currentColor.a = 1.0f;
            backImage.color = currentColor;

            charAdded = true;
        }
    }
    
    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {
        if (imageUrl.Contains("webp"))
        {
            yield return null;
        }
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
    
    public void PageMode(String mode)
    {
        Debug.Log("page mode: " + mode);
        if (mode == "details")
        {
            pageMode = "details";
            // Buttons
            charDetailsButton.gameObject.SetActive(true);
            charAddButton.gameObject.SetActive(false);

        }else if (mode == "add")
        {
            pageMode = "add";
             // Buttons
            charDetailsButton.gameObject.SetActive(false);
            charAddButton.gameObject.SetActive(true);
            charAdded = false;
        }
        else
        {
            Debug.Log("There is no page mode as: \"" + mode + "\"");
        }
    }

    public string GetInfo()
    {
        return charInfo.id;
    }

    public bool IsAdded()
    {
        return charAdded;
    }
}
