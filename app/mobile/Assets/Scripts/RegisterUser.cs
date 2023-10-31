using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;
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
    [SerializeField] private TMP_Text infoText;
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
            infoText.text = "Name and email cannot be empty";
            infoText.color = Color.red;
            return;
        }
        
        if (emailInputField.text.Contains("@") == false || emailInputField.text.Contains(".") == false)
        {
            infoText.text = "Invalid email address";    
            infoText.color = Color.red;
            return;
        }
        
        if ( passwordInputField.text.Length < 6)
        {
            infoText.text = "Password must be at least 6 characters";
            infoText.color = Color.red;
            return;
        }
        
        if(confirmPasswordInputField.text != passwordInputField.text)
        {
            infoText.text = "Passwords do not match";
            infoText.color = Color.red;
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
        
        if (request.responseCode != 200 || _useraData?.username == null)
        {
            infoText.text = "Error: ";
            infoText.color = Color.red;
        }
        else
        {
            infoText.text = "Successfully registered";
            infoText.color = Color.green;
            
            // Burada token alıp kaydetmek lazım ki login olmuş gibi olsun
            // PersistenceManager.UserToken = _useraData.token;
            PersistenceManager.UserName = _useraData.username;
            
            // 2 saniye sonra login sayfasına geç
            DOVirtual.DelayedCall(2f, () =>
            {
                canvasManager.ShowLogInPage();
                canvasManager.HideSignUpPage();
            });
            
        }
    }
    
    private void OnClickedLoginButton()
    {
        canvasManager.ShowLogInPage();
        canvasManager.HideSignUpPage();
    }
}
