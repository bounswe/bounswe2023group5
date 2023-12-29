using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.Serialization;
using UnityEngine.UI;

public class GameReview : MonoBehaviour
{
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text userNameText;
    [SerializeField] private TMP_Text reviewDescriptionText;
    [SerializeField] private TMP_Text reviewRateText;

    private string gameID;
    

    public void Init(ReviewResponse reviewInfo)
    {
        userNameText.text = reviewInfo.reviewedUser;
        reviewDescriptionText.text = reviewInfo.reviewDescription;
        gameID = reviewInfo.gameId;
        reviewRateText.text = "Rate:" + reviewInfo.rating.ToString();
    }
   
}
