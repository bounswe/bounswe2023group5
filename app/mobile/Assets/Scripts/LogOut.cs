using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.UI;

public class LogOut : MonoBehaviour
{
    
    // After the log out message is sent, the user should be navigated to 
    // another page
    [SerializeField] private CanvasManager canvasManager;
    private void Awake()
    {
        GetComponent<Button>().onClick.AddListener(DoLogOut);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    private void DoLogOut()
    {
        // Delete the token
        
        // Return back to the sign-up page, normally we'll return to log-in page
        canvasManager.ShowSignUpPage();
        canvasManager.HideHomePage();
    }
}
