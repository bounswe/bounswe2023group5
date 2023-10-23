using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class ForgetPassword : MonoBehaviour
{
    public TMP_InputField emailInputField;
    private CanvasManager canvasManager;
    [SerializeField] private TMP_Text errorMessageText;
    [SerializeField] private Button sendButton;
    [SerializeField] private Button navigateToLogin;

    private void Awake()
    {
        sendButton.onClick.AddListener(OnClickedForgetPassword);
        navigateToLogin.onClick.AddListener(OnClickedNavigateToLogin);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedForgetPassword()
    {
        errorMessageText.text = "";

        if (emailInputField.text == "" || !emailInputField.text.Contains("@") || !emailInputField.text.Contains("."))
        {
            DisplayError("Email is not valid.");
            return;
        }

        bool isEmailFound = UserDataHelper.DoesEmailExist(emailInputField.text);
        
        if(isEmailFound)
        {
            Debug.Log("Password sent to email");
            canvasManager.HideForgetPasswordPage();
            canvasManager.ShowLogInPage();
        }
        else
        {
            DisplayError("Email not found.");
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