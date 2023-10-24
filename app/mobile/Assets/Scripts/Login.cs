using System;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class Login : MonoBehaviour
{
    public TMP_InputField usernameInputField;
    public TMP_InputField passwordInputField;
    [SerializeField] private CanvasManager canvasManager;
    
    private void Awake()
    {
        GetComponent<Button>().onClick.AddListener(OnClickedLogin);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedLogin()
    {
        
    }

}