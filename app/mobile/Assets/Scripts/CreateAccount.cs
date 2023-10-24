using System;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class CreateAccount : MonoBehaviour
{
    public TMP_InputField nameInputField;
    public TMP_InputField emailInputField;
    public TMP_InputField passwordInputField;
    public TMP_InputField confirmPasswordInputField;
    private CanvasManager canvasManager;
    private void Awake()
    {
        GetComponent<Button>().onClick.AddListener(OnClickedCreateAccount);
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
}
