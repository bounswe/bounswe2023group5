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
    }
    
    public void HideHomePage()
    {
        homePage.SetActive(false);
    }
    
}
