using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class ProfileItem : MonoBehaviour
{
    public Button button;
    public TMP_Text title;
    
    private string type;
    private string id;

    public void InitGamesPage(string _title, string _id)
    {
        title.text = _title;
        button.onClick.AddListener(()=>OnClickedGameButton(_id));
    }
    
    
    public void InitGroupPage(string _title, string _id)
    {
        title.text = _title;
        button.onClick.AddListener(()=>OnClickedGroupButton(_id));
    }
    public void InitNotificationPage(string _title)
    {
        title.text = _title;
    }
    

    public void OnClickedGameButton(string id)
    {
        CanvasManager.instance.ShowGameDetailsPage(id);
    }
    
    public void OnClickedGroupButton(string id)
    {
        CanvasManager.instance.ShowGroupDetailsPage(id);
    }
    
}
