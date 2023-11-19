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
        createGameButton.onClick.AddListener(OnClickedCreateGame);
        createTagButton.onClick.AddListener(OnClickedCreateTag);
        updateTagButton.onClick.AddListener(OnClickedUpdateTag);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnEnable()
    {
        // Set all buttons' colors yellow
        createGameButton.image.color = Color.yellow;
        createTagButton.image.color = Color.yellow;
    }

    /*
    public void Init()
    {
        gameId = _gameID;
        OnClickedSummaryButton();
        GetGameSummary();
    }
    */
    
    private void OnClickedCreateGame()
    {
        // Set this button's color as blue, and others as white
        createGameButton.image.color = Color.green;
        createTagButton.image.color = Color.yellow;
        
        // Change the screen to CreateGame page
        canvasManager.ShowCreateGamePage();
    }
    private void OnClickedCreateTag()
    {
        // Set this button's color as blue, and others as white
        createGameButton.image.color = Color.yellow;
        createTagButton.image.color = Color.green;
        
        // Change the screen to CreateGame page
        canvasManager.ShowCreateTagPage();
    }
    
    private void OnClickedUpdateTag()
    {
        // Set this button's color as blue, and others as white
        createGameButton.image.color = Color.yellow;
        createTagButton.image.color = Color.green;
        
        // Change the screen to CreateGame page
        canvasManager.ShowUpdateTagPage();
    }
    
    
}
