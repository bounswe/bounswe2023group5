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

public class AdminControlPanelScript : MonoBehaviour
{

    [SerializeField] private Button createGameButton;
    [SerializeField] private Button createTagButton;
    [SerializeField] private Button updateTagButton;
    [SerializeField] private Button deleteTagButton;
    [SerializeField] private Button banUserButton;
    [SerializeField] private Button makeAdminButton;
    private CanvasManager canvasManager;

    
    private void Awake()
    {
        createGameButton.onClick.AddListener(OnClickedCreateGameButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    /*
    public void Init()
    {
        gameId = _gameID;
        OnClickedSummaryButton();
        GetGameSummary();
    }
    */
    
    private void OnClickedCreateGameButton()
    {
        // Set this button's color as blue, and others as white
        createGameButton.image.color = Color.blue;
        
        // Change the screen to CreateGame page
        canvasManager.ShowCreateGamePage();
    }
    
    
    
}
