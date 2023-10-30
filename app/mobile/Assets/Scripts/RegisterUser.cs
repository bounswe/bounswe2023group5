using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.UI;

public class RegisterUser : MonoBehaviour
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
        createAccountButton.onClick.AddListener(Register);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }
  

    private void Register()
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
        string url = AppVariables.HttpServerUrl + "/auth/register";
        var registerData = new RegisterRequest();
        registerData.username = nameInputField.text;
        registerData.email = emailInputField.text;
        registerData.password = passwordInputField.text;
        string bodyJsonString = JsonConvert.SerializeObject(registerData);
        StartCoroutine(Post(url, bodyJsonString));
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        var _useraData = JsonConvert.DeserializeObject<RegisterResponse>(response);
        Debug.Log(_useraData.email + " " + _useraData.id + " " + _useraData.username + " " + _useraData.role + " " + _useraData.verified + " " + _useraData.isDeleted + " " + _useraData.createdAt);
        Debug.Log("Status Code: " + request.responseCode);
        if (request.responseCode == 200)
        {
            canvasManager.ShowHomePage();
            canvasManager.HideSignUpPage();
        }
    }
    
    private void OnClickedLoginButton()
    {
        canvasManager.ShowLogInPage();
        canvasManager.HideLogInPage();
    }
}
