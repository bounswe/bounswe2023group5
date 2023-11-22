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
    
    private CanvasManager canvasManager;


    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(GroupResponse groupInfo)
    {
        title.text = groupInfo.title;
        describtion.text = groupInfo.description;
        membershipPolicy.text = groupInfo.membershipPolicy;
        //tags.text = groupInfo.tags;
        quota.text = groupInfo.quota.ToString();
        //members.text = groupInfo.members;
    }

    
    
    private void OnClickedGameDetailsButton()
    {
        // Debug.Log("Game Details Button Clicked");
    }
}