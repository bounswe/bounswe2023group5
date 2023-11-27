using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class GroupPage : MonoBehaviour
{
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text describtion;
    [SerializeField] private TMP_Text membershipPolicy;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text quota;
    [SerializeField] private TMP_Text members;
    [SerializeField] private Button groupDetailsButton;

    private string groupId;
    
    private CanvasManager canvasManager;


    private void Awake()
    {
        groupDetailsButton.onClick.AddListener(OnClickedGroupDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(GroupResponse groupInfo)
    {
        title.text = groupInfo.title;
        describtion.text = groupInfo.description;
        membershipPolicy.text = groupInfo.membershipPolicy;
        //tags.text = groupInfo.tags;
        quota.text = groupInfo.quota.ToString();
        groupId = groupInfo.id;
        //members.text = groupInfo.members;
    }

    
    
    private void OnClickedGroupDetailsButton()
    {
        // Debug.Log("Game Details Button Clicked");
        canvasManager.ShowGroupDetailsPage(groupId);
    }
}