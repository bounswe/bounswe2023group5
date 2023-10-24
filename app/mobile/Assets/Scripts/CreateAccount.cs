using System;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class CreateAccount : MonoBehaviour
{
    [SerializeField] private TMP_InputField nameInputField;
    [SerializeField] private TMP_InputField emailInputField;
    [SerializeField] private TMP_InputField passwordInputField;
    [SerializeField] private TMP_InputField confirmPasswordInputField;
    [SerializeField] private Button createAccountButton;
    [SerializeField] private Button loginButton;
    private CanvasManager canvasManager;
    private void Awake()
    {
        loginButton.onClick.AddListener(OnClickedLoginButton);
        createAccountButton.onClick.AddListener(OnClickedCreateAccount);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedCreateAccount()
    {
        if (nameInputField.text == "" || emailInputField.text == "")
        {
            Debug.Log("Name and email cannot be empty");
            return;
        }
        if ( passwordInputField.text.Length < 6)
        {
            Debug.Log("Password must be at least 6 characters");
            return;
        }
        if(confirmPasswordInputField.text != passwordInputField.text)
        {
            Debug.Log("Password does not match");
            return;
        }
        
        if (emailInputField.text.Contains("@") == false || emailInputField.text.Contains(".") == false)
        {
            Debug.Log("Email is not valid");
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.name = nameInputField.text;
        userInfo.email = emailInputField.text;
        userInfo.password = passwordInputField.text;
        var isUserCreated = UserDataHelper.WriteNewUserData(userInfo);
        if (isUserCreated)
        {
            canvasManager.ShowHomePage();
            canvasManager.HideSignUpPage();
        }
        Debug.Log(isUserCreated ? "Account created" : "Account not created");
    }

    private void OnClickedLoginButton()
    {
        canvasManager.ShowLogInPage();
        canvasManager.HideLogInPage();
    }
}
