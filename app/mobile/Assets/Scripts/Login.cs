using System;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class Login : MonoBehaviour
{
    public TMP_InputField usernameInputField;
    public TMP_InputField passwordInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button loginButton;
    [SerializeField] private Button forgetPasswordButton; 
    [SerializeField] private Button signupButton;
    
    private void Awake()
    {
        loginButton.onClick.AddListener(OnClickedLogin);
        forgetPasswordButton.onClick.AddListener(OnClickedForgetPassword);
        signupButton.onClick.AddListener(OnClickedSignup);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedLogin()
    {
        bool isEmail =  usernameInputField.text.Contains('@');
        
       
    }

    private void OnClickedForgetPassword()
    {
        canvasManager.ShowForgetPasswordPage();
        canvasManager.HideLogInPage();
    }
    private void OnClickedSignup()
    {
        canvasManager.ShowSignUpPage();
        canvasManager.HideLogInPage();
    }
    

}