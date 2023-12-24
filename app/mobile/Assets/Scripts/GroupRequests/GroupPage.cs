using System;
using System.Collections;
using System.Linq;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class GroupPage : MonoBehaviour
{
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
        //members.text = groupInfo.members;
    }

    
    
    private void OnClickedGroupDetailsButton()
    {
        // Debug.Log("Game Details Button Clicked");
        canvasManager.ShowGroupDetailsPage(groupId);
    }
}