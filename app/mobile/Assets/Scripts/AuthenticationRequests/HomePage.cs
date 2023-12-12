using UnityEngine;
using UnityEngine.UI;

public class HomePage: MonoBehaviour
{
    private CanvasManager canvasManager;
    [SerializeField] private Button gamesPageButton;
    [SerializeField] private Button loginPageButton;
    [SerializeField] private Button signUpPageButton;
    [SerializeField] private Button forgetPasswordPageButton;
    [SerializeField] private Button resetPasswordPageButton;
    [SerializeField] private Button changePasswordPageButton;

    private void Awake()
    {
        canvasManager = FindObjectOfType<CanvasManager>();
        gamesPageButton.onClick.AddListener(canvasManager.ShowGamesPage);
        loginPageButton.onClick.AddListener(canvasManager.ShowLogInPage);
        signUpPageButton.onClick.AddListener(canvasManager.ShowSignUpPage);
        forgetPasswordPageButton.onClick.AddListener(canvasManager.ShowForgetPasswordPage);
        resetPasswordPageButton.onClick.AddListener(canvasManager.ShowResetPasswordPage);
        changePasswordPageButton.onClick.AddListener(canvasManager.ShowChangePasswordPage);
    }
}