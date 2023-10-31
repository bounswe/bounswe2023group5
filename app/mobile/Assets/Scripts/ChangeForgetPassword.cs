using System.Collections;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.UI;

public class ChangeForgetPassword : MonoBehaviour
{
    public TMP_InputField emailInputField;
    public TMP_InputField codeInputField;
    public TMP_InputField newPasswordInputField;
    [SerializeField] private TMP_Text errorMessageText;
    [SerializeField] private Button changePasswordButton;
    [SerializeField] private Button navigateToLogin;
    private CanvasManager canvasManager;
    private string userToken;

    private void Awake()
    {
        changePasswordButton.onClick.AddListener(OnClickedChangePassword);
        navigateToLogin.onClick.AddListener(OnClickedNavigateToLogin);
        canvasManager = FindObjectOfType<CanvasManager>();
    }

    private void OnClickedChangePassword()
    {
        errorMessageText.text = "";

        if (string.IsNullOrEmpty(emailInputField.text) || !emailInputField.text.Contains("@") || !emailInputField.text.Contains("."))
        {
            DisplayError("Email is not valid.");
            return;
        }

        if (string.IsNullOrEmpty(codeInputField.text))
        {
            DisplayError("Code cannot be empty.");
            return;
        }

        if (string.IsNullOrEmpty(newPasswordInputField.text) || newPasswordInputField.text.Length < 6)
        {
            DisplayError("Password must be at least 6 characters.");
            return;
        }

        string url = AppVariables.HttpServerUrl + "/auth/verify-reset-code";
        var verifyCodeData = new VerifyResetCodeRequest { userEmail = emailInputField.text, resetCode = codeInputField.text };
        string bodyJsonString = JsonConvert.SerializeObject(verifyCodeData);
        StartCoroutine(VerifyResetCode(url, bodyJsonString));
    }

    IEnumerator VerifyResetCode(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        
        var responseContent = request.downloadHandler.text;
        Debug.Log("Response Content: " + responseContent);

        long statusCode = request.responseCode;
        Debug.Log("Status Code: " + statusCode);   
        
        if (statusCode == 200)
        {
            userToken = responseContent;
            ChangeForgotPassword();
        }
        else
        {
            DisplayError("Invalid reset code.");
        }
    }

    private void ChangeForgotPassword()
    {
        string url = AppVariables.HttpServerUrl + "/auth/change-forgot-password";
        var changePasswordData = new ChangeForgetPasswordRequest { newPassword = newPasswordInputField.text };
        string bodyJsonString = JsonConvert.SerializeObject(changePasswordData);
        StartCoroutine(ChangePassword(url, bodyJsonString));
    }

    IEnumerator ChangePassword(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", userToken);
        yield return request.SendWebRequest();
        
        if (request.responseCode == 200)
        {
            canvasManager.ShowLogInPage();
            canvasManager.HideChangeForgetPasswordPage();
        }
        else
        {
            DisplayError("Error changing the password.");
        }
    }

    private void OnClickedNavigateToLogin()
    {
        canvasManager.HideChangeForgetPasswordPage();
        canvasManager.ShowLogInPage();
    }

    private void DisplayError(string message)
    {
        errorMessageText.text = message;
    }
}
