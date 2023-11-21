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

public class ProfileScript : MonoBehaviour
{
    [SerializeField] private Button adminPanelButton;
    private CanvasManager canvasManager;
    private void OnEnable()
    {
        // If she is admin, make Admin Control Panel button visible
        // and add listener for clicking
        if (PersistenceManager.Role == "ADMIN")
        {
            Debug.Log("Role: "+ PersistenceManager.Role);
            adminPanelButton.gameObject.SetActive(true);
            adminPanelButton.onClick.AddListener(onClickOpenAdminPanel);
            canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        }
        // Otherwise make it invisible
        else
        {
            Debug.Log("Role: "+ PersistenceManager.Role);
            
            adminPanelButton.gameObject.SetActive(false);
            
        }
    }

    private void onClickOpenAdminPanel()
    {
        canvasManager.ShowAdminControlPanelPage();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
