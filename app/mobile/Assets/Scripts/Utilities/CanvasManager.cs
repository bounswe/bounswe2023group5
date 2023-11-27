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
    [SerializeField] private GameObject deleteAccountPage;
    [SerializeField] private GameObject adminControlPanelPage;
    [SerializeField] private GameObject createGamePage;
    [SerializeField] private GameObject createTagPage;
    [SerializeField] private GameObject updateTagPage;
    [SerializeField] private GameObject deleteTagPage;
    [SerializeField] private GameObject groupDetailsPage;
    [SerializeField] private GameObject groupsPage;
    public GameObject postComments;
    
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
    
    public void HideProfilePage()
    {
        profilePage.SetActive(false);
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
        gameDetailsPage.GetComponent<GameDetails>().Init(gameID);
    }
    
    public void HideGameDetailsPage()
    {
        gameDetailsPage.SetActive(false);
    }
    
    public void ShowDeleteAccountPage()
    {
        deleteAccountPage.SetActive(true);
    }
    
    public void HideDeleteAccountPage()
    {
        deleteAccountPage.SetActive(false);
    }

    public void ShowCreateGamePage()
    {
        adminControlPanelPage.SetActive(false);
        createGamePage.SetActive(true);
    }
    
    public void ShowCreateTagPage()
    {
        adminControlPanelPage.SetActive(false);
        createTagPage.SetActive(true);
    }

    public void HideCreateTagPage()
    {
        adminControlPanelPage.SetActive(true);
        createTagPage.SetActive(false);
    }

    public void ShowAdminControlPanelPage()
    {
        adminControlPanelPage.SetActive(true);
        profilePage.SetActive(false);
    }

    public void ShowUpdateTagPage()
    {
        adminControlPanelPage.SetActive(false);
        updateTagPage.SetActive(true);
    }
    
    public void HideUpdateTagPage()
    {
        adminControlPanelPage.SetActive(true);
        updateTagPage.SetActive(false);
    }
    
    public void ShowDeleteTagPage()
    {
        adminControlPanelPage.SetActive(false);
        deleteTagPage.SetActive(true);
    }
    
    public void HideDeleteTagPage()
    {
        adminControlPanelPage.SetActive(true);
        deleteTagPage.SetActive(false);
    }

    public void ShowGroupDetailsPage(string groupId)
    {
        groupDetailsPage.SetActive(true);
        groupsPage.SetActive(false);
        groupDetailsPage.GetComponent<GroupDetails>().Init(groupId);
    }

    public void HideGroupDetailsPage()
    {
        groupDetailsPage.SetActive(false);
    }

    public void ShowGroupsPage()
    {
        groupsPage.SetActive(true);
    }

    public void HideGroupsPage()
    {
        groupsPage.SetActive(false);
    }
    
    public void ShowPostComments()
    {
        postComments.SetActive(true);
    }
    
    public void HidePostComments()
    {
        postComments.SetActive(false);
    }
    
}
