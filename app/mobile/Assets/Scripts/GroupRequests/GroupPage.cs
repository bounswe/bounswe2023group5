using System;
using System.Collections;
using System.Linq;
using JetBrains.Annotations;
using UnityEngine;
using TMPro;
using Unity.VisualScripting;
using UnityEngine.UI;
using UnityEngine.Networking;

public class GroupPage : MonoBehaviour
{
    [SerializeField] private Image image;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text describtion;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text dateText;
    [SerializeField] private TMP_Text members;
    [SerializeField] private Button groupDetailsButton;

    private string groupId;
    
    private CanvasManager canvasManager;


    private void Awake()
    {
        groupDetailsButton.onClick.AddListener(OnClickedGroupDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(GroupGetAllResponse groupInfo)
    {
        title.text = groupInfo.title;
        describtion.text = groupInfo.description;
        //tags.text = groupInfo.tags;
        string date = groupInfo.createdAt;
        dateText.text = "since " + date.Substring(8, 2) + "/" + date.Substring(5, 2) + "/" + date.Substring(0, 4);
        members.text = groupInfo.members.Length.ToString();
        groupId = groupInfo.id;
        // var url = AppVariables.HttpImageUrl + groupInfo.groupIcon;
        // if (groupInfo.groupIcon != null) StartCoroutine(LoadImageFromURL(url, image));
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
    
    private void OnClickedGroupDetailsButton()
    {
        // Debug.Log("Group Details Button Clicked");
        canvasManager.ShowGroupDetailsPage(groupId);
    }
}