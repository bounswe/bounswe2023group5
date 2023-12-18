using System;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using TMPro;
using Newtonsoft.Json;
using System.Collections;
using System.Text;
using DG.Tweening;

public class Login : MonoBehaviour
{
    public TMP_InputField emailInputField;
    public TMP_InputField passwordInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button loginButton;
    [SerializeField] private Button forgetPasswordButton; 
    [SerializeField] private Button signupButton;
    [SerializeField] private TMP_Text infoText;
    
    private void Start()
    {
        loginButton.onClick.AddListener(OnClickedLogin);
        forgetPasswordButton.onClick.AddListener(OnClickedForgetPassword);
        signupButton.onClick.AddListener(OnClickedSignup);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        if (!string.IsNullOrEmpty(PersistenceManager.UserToken))
        {
            canvasManager.ShowGamesPage();
            canvasManager.HideLogInPage();
        }
        
    }

    // This function  will be called whenever the page is set active
    private void OnEnable()
    {
        infoText.text = "";
    }

    private void OnClickedLogin()
    {
        if (emailInputField.text == "")
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
        
        string url = AppVariables.HttpServerUrl + "/auth/login";
        var loginData = new LoginRequest();
        loginData.email = emailInputField.text;
        loginData.password = passwordInputField.text;
        string bodyJsonString = JsonConvert.SerializeObject(loginData);
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
        var _loginResponseData = JsonConvert.DeserializeObject<LoginResponse>(response);
        var _useraData = _loginResponseData?.user;
        
        if (request.responseCode != 200 || _useraData == null || _useraData?.username == null)
        {
            infoText.text = "Error";
            infoText.color = Color.red;
        }
        else
        {
            infoText.text = "Successfully logined";
            infoText.color = Color.green;
            
            PersistenceManager.UserToken = _loginResponseData.token;
            PersistenceManager.UserName = _useraData.username;
            PersistenceManager.id = _useraData.id;
            PersistenceManager.Password = passwordInputField.text;
            PersistenceManager.Role = _useraData.role;
            
            DOVirtual.DelayedCall(2f, () =>
            {
                canvasManager.HideLogInPage();
                canvasManager.ShowGamesPage();
            });
            
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
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