using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class RecentActivities : MonoBehaviour
{
    // public TMP_Text achievementTitle;
    public TMP_Text description;
    public TMP_Text title;
    
    public void Init(string _description, string _type)
    {
        switch (_type)
        {
            case "ACHIEVEMENT":
                title.text = "You have unlocked an achievement";
                break;
            case "REVIEW":
                title.text = "You have written a review";
                break;
            case "GROUP":
                title.text = "You have created a group";
                break;
            case "GAME":
                title.text = "You have created a game";
                break;
            case "COMMENT":
                title.text = "You have commented";
                break;
            case "VOTE":
                title.text = "You have voted on a game";
                break;
            case "POST":
                title.text = "You have created a post";
                break;
        }
        description.text = _description;
    }
    
}
