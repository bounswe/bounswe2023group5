using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Serialization;

public class CanvasManager : MonoBehaviour
{
    [SerializeField] private GameObject signUpPage;
    [SerializeField] private GameObject logInPage;
    [SerializeField] private GameObject forgetPasswordPage;
    [SerializeField] private GameObject resetPasswordPage;
    [SerializeField] private GameObject homePage;
    [SerializeField] private GameObject forumPage;
    [SerializeField] private GameObject gamesPage;
    [SerializeField] private GameObject profilePage;
    [SerializeField] private GameObject changePasswordPage;
    [SerializeField] private GameObject changeForgetPasswordPage;
    [SerializeField] private GameObject gameDetailsPage;


    
    public void ShowSignUpPage()
    {
        signUpPage.SetActive(true);
    }
    
    public void HideSignUpPage()
    {
        signUpPage.SetActive(false);
    }

    public void ShowLogInPage()
    {
        logInPage.SetActive(true);
    }
    
    public void HideLogInPage()
    {
        logInPage.SetActive(false);
    }

    public void ShowForgetPasswordPage()
    {
        forgetPasswordPage.SetActive(true);
    }
    
    public void HideForgetPasswordPage()
    {
        forgetPasswordPage.SetActive(false);
    }

    
    public void ShowChangePasswordPage()
    {
        changePasswordPage.SetActive(true);
    }
    
    public void HideChangePasswordPage()
    {
        changePasswordPage.SetActive(false);
    }
    
    public void ShowChangeForgetPasswordPage()
    {
        changeForgetPasswordPage.SetActive(true);
    }
    
    public void HideChangeForgetPasswordPage()
    {
        changeForgetPasswordPage.SetActive(false);
    }
    
    public void ShowResetPasswordPage()
    {
        resetPasswordPage.SetActive(true);
    }
    
    public void HideResetPasswordPage()
    {
        resetPasswordPage.SetActive(false);
    }
    
    public void ShowHomePage()
    {
        homePage.SetActive(true);
        forumPage.SetActive(false);
        gamesPage.SetActive(false);
        profilePage.SetActive(false);
    }

    public void HideHomePage()
    {
        homePage.SetActive(false);
    }
    
    
    public void ShowForumPage()
    {
        forumPage.SetActive(true);
        homePage.SetActive(false);
        gamesPage.SetActive(false);
        profilePage.SetActive(false);
    }
    
    public void ShowGamesPage()
    {
        gamesPage.SetActive(true);
        homePage.SetActive(false);
        forumPage.SetActive(false);
        profilePage.SetActive(false);
    }
    
    public void ShowProfilePage()
    {
        profilePage.SetActive(true);
        homePage.SetActive(false);
        forumPage.SetActive(false);
        gamesPage.SetActive(false);
    }
    
    public void ShowGameDetailsPage(string gameID)
    {
        gameDetailsPage.SetActive(true);
        gameDetailsPage.GetComponent<GameDetails>().GameDetail(gameID);
    }
    
    public void HideGameDetailsPage()
    {
        gameDetailsPage.SetActive(false);
    }
    
    

}
