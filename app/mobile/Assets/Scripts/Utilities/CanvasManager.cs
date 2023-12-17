using System;
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
    [SerializeField] private GameObject editProfilePage;
    [SerializeField] private GameObject createEditPostPage;
    public GameObject postComments;
    [SerializeField] private GameObject commentComments;
    private GameObject currentActivePage;

    private void Awake()
    {
        currentActivePage = logInPage;
    }

    private bool iQuit = false;

    void Update()
    {
        if (iQuit == true)
        {
            if (Input.GetKeyDown(KeyCode.Escape))
            {
                Application.Quit();
            }
        }
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            iQuit = true;
            StartCoroutine(QuitingTimer());
        }
    }

    IEnumerator QuitingTimer()
    {
        yield return new WaitForSeconds(3);
        iQuit = false;
    }

    public void ShowSignUpPage()
    {
        signUpPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = signUpPage;
    }
    
    public void HideSignUpPage()
    {
        signUpPage.SetActive(false);
    }

    public void ShowLogInPage()
    {
        currentActivePage?.SetActive(false);
        logInPage.SetActive(true);
        currentActivePage = logInPage;
    }
    
    public void HideLogInPage()
    {
        logInPage.SetActive(false);
    }

    public void ShowForgetPasswordPage()
    {
        forgetPasswordPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = forgetPasswordPage;
    }
    
    public void HideForgetPasswordPage()
    {
        forgetPasswordPage.SetActive(false);
    }

    
    public void ShowChangePasswordPage()
    {
        changePasswordPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = changePasswordPage;
    }
    
    public void HideChangePasswordPage()
    {
        changePasswordPage.SetActive(false);
    }
    
    public void ShowChangeForgetPasswordPage()
    {
        changeForgetPasswordPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = changeForgetPasswordPage;
    }
    
    public void HideChangeForgetPasswordPage()
    {
        changeForgetPasswordPage.SetActive(false);
    }
    
    public void ShowResetPasswordPage()
    {
        resetPasswordPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = resetPasswordPage;
    }
    
    public void HideResetPasswordPage()
    {
        resetPasswordPage.SetActive(false);
    }
    
    public void ShowHomePage()
    {
        homePage.SetActive(true);
        currentActivePage?.SetActive(false);
        currentActivePage = homePage;
    }

    public void HideHomePage()
    {
        homePage.SetActive(false);
    }
    
    public void HideProfilePage()
    {
        profilePage.SetActive(false);
    }
    
    
    public void ShowGroupPage()
    {
        groupsPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = groupsPage;
    }
    
    public void ShowGamesPage()
    {
        currentActivePage?.SetActive(false);
        gamesPage.SetActive(true);
        currentActivePage = gamesPage;
    }
    
    public void ShowProfilePage()
    {
        profilePage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = profilePage;
    }
    
    public void ShowGameDetailsPage(string gameID)
    {
        gameDetailsPage.SetActive(true);
        gameDetailsPage.GetComponent<GameDetails>().Init(gameID);
        currentActivePage.SetActive(false);
        currentActivePage = gameDetailsPage;
    }
    
    public void HideGameDetailsPage()
    {
        gameDetailsPage.SetActive(false);
    }
    
    public void ShowDeleteAccountPage()
    {
        deleteAccountPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = deleteAccountPage;
    }
    
    public void HideDeleteAccountPage()
    {
        deleteAccountPage.SetActive(false);
    }

    public void ShowCreateGamePage()
    {
        adminControlPanelPage.SetActive(false);
        createGamePage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = createGamePage;
    }
    
    public void ShowCreateTagPage()
    {
        adminControlPanelPage.SetActive(false);
        createTagPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = createTagPage;
    }

    public void HideCreateTagPage()
    {
        adminControlPanelPage.SetActive(true);
        createTagPage.SetActive(false);
        currentActivePage.SetActive(false);
        currentActivePage = adminControlPanelPage;
    }

    public void ShowAdminControlPanelPage()
    {
        adminControlPanelPage.SetActive(true);
        profilePage.SetActive(false);
        currentActivePage.SetActive(false);
        currentActivePage = adminControlPanelPage;
    }

    public void ShowUpdateTagPage()
    {
        adminControlPanelPage.SetActive(false);
        updateTagPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = updateTagPage;
    }
    
    public void HideUpdateTagPage()
    {
        adminControlPanelPage.SetActive(true);
        updateTagPage.SetActive(false);
        currentActivePage.SetActive(false);
        currentActivePage = adminControlPanelPage;
    }
    
    public void ShowDeleteTagPage()
    {
        adminControlPanelPage.SetActive(false);
        deleteTagPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = deleteTagPage;
    }
    
    public void HideDeleteTagPage()
    {
        adminControlPanelPage.SetActive(true);
        deleteTagPage.SetActive(false);
        currentActivePage.SetActive(false);
        currentActivePage = adminControlPanelPage;
    }

    public void ShowGroupDetailsPage(string groupId)
    {
        groupDetailsPage.SetActive(true);
        groupsPage.SetActive(false);
        groupDetailsPage.GetComponent<GroupDetails>().Init(groupId);
        currentActivePage.SetActive(false);
        currentActivePage = groupDetailsPage;
    }

    public void HideGroupDetailsPage()
    {
        groupDetailsPage.SetActive(false);
    }

    public void ShowGroupsPage()
    {
        groupsPage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = groupsPage;
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
    
    public void ShowCommentComments()
    {
        commentComments.SetActive(true);
    }
    
    public void HideCommentComments()
    {
        commentComments.SetActive(false);
    }
    
    public void ShowEditProfilePage()
    {
        editProfilePage.SetActive(true);
        currentActivePage.SetActive(false);
        currentActivePage = editProfilePage;
    }
    
    public void ShowCreateEditPostPage()
    {
        createEditPostPage.SetActive(true);
    }
    
    public void HideCreateEditPostPage()
    {
        createEditPostPage.SetActive(false);
    }
    
}
