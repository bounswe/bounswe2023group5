using System.Collections;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.UI;

public class ForgetPassword : MonoBehaviour
{
    public TMP_InputField emailInputField;
    [SerializeField] private TMP_Text errorMessageText;
    [SerializeField] private Button sendButton;
    [SerializeField] private Button navigateToLogin;
    private CanvasManager canvasManager;

    private void Awake()
    {
        sendButton.onClick.AddListener(OnClickedForgetPassword);
        navigateToLogin.onClick.AddListener(OnClickedNavigateToLogin);
        canvasManager = FindObjectOfType<CanvasManager>();
    }

    private void OnClickedForgetPassword()
    {
        errorMessageText.text = "";

        if (string.IsNullOrEmpty(emailInputField.text) || !emailInputField.text.Contains("@") || !emailInputField.text.Contains("."))
        {
            DisplayError("Email is not valid.");
            return;
        }

        string url = AppVariables.HttpServerUrl + "/auth/forgot-password";
        var forgetPasswordData = new ForgetPasswordRequest { email = emailInputField.text };
        string bodyJsonString = JsonConvert.SerializeObject(forgetPasswordData);
        StartCoroutine(Post(url, bodyJsonString));
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();

        if (request.responseCode == 200)
        {
            canvasManager.ShowChangeForgetPasswordPage();
            canvasManager.HideForgetPasswordPage();
        }
        else
        {
            DisplayError("Error sending the reset password email.");
        }
    }

    private void OnClickedNavigateToLogin()
    {
        canvasManager.HideForgetPasswordPage();
        canvasManager.ShowLogInPage();
    }

    private void DisplayError(string message)
    {
        errorMessageText.text = message;
    }
}
