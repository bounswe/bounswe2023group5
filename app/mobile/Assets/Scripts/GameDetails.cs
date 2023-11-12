using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.Serialization;
using UnityEngine.UI;

public class GameDetails : MonoBehaviour
{

    [SerializeField] private Button summaryButton;
    [SerializeField] private Button reviewsButton;
    [SerializeField] private Button forumButton;
    [FormerlySerializedAs("summaryController")] [SerializeField] private SummaryManager summaryManager;
    [FormerlySerializedAs("reviewsController")] [SerializeField] private ReviewsManager reviewsManager;
    [FormerlySerializedAs("forumController")] [SerializeField] private ForumManager forumManager;
    [SerializeField] private Button exitButton;
    private string gameId;
    
    private CanvasManager canvasManager;
    

    
    private void Awake()
    {
        summaryButton.onClick.AddListener(OnClickedSummaryButton);
        reviewsButton.onClick.AddListener(OnClickedReviewsButton);
        forumButton.onClick.AddListener(OnClickedForumButton);
        exitButton.onClick.AddListener(OnClickedExitButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(string _gameID)
    {
        gameId = _gameID;
        OnClickedSummaryButton();
    }

    
    
    private void OnClickedExitButton()
    {
        canvasManager.ShowGamesPage();
        canvasManager.HideGameDetailsPage();
    }
    
    private void OnClickedSummaryButton()
    {
        summaryButton.image.color = Color.blue;
        reviewsButton.image.color = Color.white;
        forumButton.image.color = Color.white;
        
        summaryManager.gameObject.SetActive(true);
        reviewsManager.gameObject.SetActive(false);
        forumManager.gameObject.SetActive(false);
        summaryManager.Inıt(gameId);
    }
    
    private void OnClickedReviewsButton()
    {
        summaryButton.image.color = Color.white;
        reviewsButton.image.color = Color.blue;
        forumButton.image.color = Color.white;
        
        summaryManager.gameObject.SetActive(false);
        reviewsManager.gameObject.SetActive(true);
        forumManager.gameObject.SetActive(false);
        reviewsManager.Init(gameId);
    }
    
    private void OnClickedForumButton()
    {
        summaryButton.image.color = Color.white;
        reviewsButton.image.color = Color.white;
        forumButton.image.color = Color.blue;
        
        summaryManager.gameObject.SetActive(false);
        reviewsManager.gameObject.SetActive(false);
        forumManager.gameObject.SetActive(true);
    }
    
    
}
