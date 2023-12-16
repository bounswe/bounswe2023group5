using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class RecentActivities : MonoBehaviour
{
    // public TMP_Text achievementTitle;
    public TMP_Text description;
    
    public void Init(string _description)
    {
        // achievementTitle.text = achievementResponse.title;
        description.text = _description;
    }
    
}
