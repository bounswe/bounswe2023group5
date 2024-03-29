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
    public GameObject gameDetailsPage;
    [SerializeField] private GameObject deleteAccountPage;
    [SerializeField] private GameObject adminControlPanelPage;
    [SerializeField] private GameObject createGamePage;
    [SerializeField] private GameObject createTagPage;
    [SerializeField] private GameObject updateTagPage;
    [SerializeField] private GameObject deleteTagPage;
    public GameObject groupDetailsPage;
    [SerializeField] private GameObject groupsPage;
    [SerializeField] private GameObject editProfilePage;
    [SerializeField] private GameObject createEditPostPage;
    public GameObject postComments;
    public GameObject commentComments;
    [SerializeField] private GameObject charactersPage;
    [SerializeField] private GameObject characterDetailsPage;
    [SerializeField] private GameObject createCharacterPage;
    private GameObject currentActivePage;
    private GameObject previousActivePage;
    
    public static CanvasManager instance;
    private void Awake()
    {
        currentActivePage = logInPage;
        previousActivePage = logInPage;
        if (instance == null)
        {
            instance = this;
            DontDestroyOnLoad(gameObject);
        }
        else
        {
            DestroyImmediate(gameObject);
        }
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
            currentActivePage.SetActive(false);
            previousActivePage.SetActive(true);
            currentActivePage = previousActivePage;
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
        currentActivePage.SetActive(false);
        signUpPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = signUpPage;
    }
    
    public void HideSignUpPage()
    {
        signUpPage.SetActive(false);
    }

    public void ShowLogInPage()
    {
        currentActivePage.SetActive(false);
        logInPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = logInPage;
    }
    
    public void HideLogInPage()
    {
        logInPage.SetActive(false);
    }

    public void ShowForgetPasswordPage()
    {
        currentActivePage.SetActive(false);
        forgetPasswordPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = forgetPasswordPage;
    }
    
    public void HideForgetPasswordPage()
    {
        forgetPasswordPage.SetActive(false);
    }

    
    public void ShowChangePasswordPage()
    {
        currentActivePage.SetActive(false);
        changePasswordPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = changePasswordPage;
    }
    
    public void HideChangePasswordPage()
    {
        changePasswordPage.SetActive(false);
    }
    
    public void ShowChangeForgetPasswordPage()
    {
        currentActivePage.SetActive(false);
        changeForgetPasswordPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = changeForgetPasswordPage;
    }
    
    public void HideChangeForgetPasswordPage()
    {
        changeForgetPasswordPage.SetActive(false);
    }
    
    public void ShowResetPasswordPage()
    {
        currentActivePage.SetActive(false);
        resetPasswordPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = resetPasswordPage;
    }
    
    public void HideResetPasswordPage()
    {
        resetPasswordPage.SetActive(false);
    }
    
    public void ShowHomePage()
    {
        currentActivePage.SetActive(false);
        homePage.SetActive(true);
        previousActivePage = currentActivePage;
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

    public void ShowGamesPage()
    {
        currentActivePage.SetActive(false);
        gamesPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = gamesPage;
    }
    
    public void ShowProfilePage()
    {
        currentActivePage.SetActive(false);
        profilePage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = profilePage;
    }
    
    public void ShowGameDetailsPage(string gameID)
    {
        currentActivePage.SetActive(false);
        gameDetailsPage.SetActive(true);
        gameDetailsPage.GetComponent<GameDetails>().Init(gameID);
        previousActivePage = currentActivePage;
        currentActivePage = gameDetailsPage;
    }
    
    public void HideGameDetailsPage()
    {
        currentActivePage.SetActive(false);
        gameDetailsPage.SetActive(false);
        if (previousActivePage == gameDetailsPage)
        {
            previousActivePage = gamesPage;
        }
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }
    
    public void ShowDeleteAccountPage()
    {
        currentActivePage.SetActive(false);
        deleteAccountPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = deleteAccountPage;
    }
    
    public void HideDeleteAccountPage()
    {
        deleteAccountPage.SetActive(false);
    }

    public void ShowCreateGamePage()
    {
        currentActivePage.SetActive(false);
        adminControlPanelPage.SetActive(false);
        createGamePage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = createGamePage;
    }
    
    public void ShowCreateTagPage()
    {
        currentActivePage.SetActive(false);
        adminControlPanelPage.SetActive(false);
        createTagPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = createTagPage;
    }

    public void HideCreateTagPage()
    {
        adminControlPanelPage.SetActive(true);
        createTagPage.SetActive(false);
        previousActivePage = currentActivePage;
        currentActivePage = adminControlPanelPage;
    }

    public void ShowAdminControlPanelPage()
    {
        currentActivePage.SetActive(false);
        adminControlPanelPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = adminControlPanelPage;
    }

    public void ShowUpdateTagPage()
    {
        currentActivePage.SetActive(false);
        updateTagPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = updateTagPage;
    }
    
    public void ShowDeleteTagPage()
    {
        adminControlPanelPage.SetActive(false);
        deleteTagPage.SetActive(true);
        currentActivePage.SetActive(false);
        previousActivePage = currentActivePage;
        currentActivePage = deleteTagPage;
    }
    
    public void HideDeleteTagPage()
    {
        adminControlPanelPage.SetActive(true);
        deleteTagPage.SetActive(false);
        previousActivePage = currentActivePage;
        currentActivePage = adminControlPanelPage;
    }

    public void ShowGroupDetailsPage(string groupId)
    {
        currentActivePage.SetActive(false);
        groupDetailsPage.SetActive(true);
        groupsPage.SetActive(false);
        groupDetailsPage.GetComponent<GroupDetails>().Init(groupId);
        previousActivePage = currentActivePage;
        currentActivePage = groupDetailsPage;
    }

    public void HideGroupDetailsPage()
    {
        currentActivePage.SetActive(false);
        groupDetailsPage.SetActive(false);
        if (previousActivePage == groupDetailsPage)
        {
            previousActivePage = groupsPage;
        }
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }

    public void ShowGroupsPage()
    {
        currentActivePage.SetActive(false);
        groupsPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = groupsPage;
    }
    
    
    public void ShowPostComments(string id)
    {
        currentActivePage.SetActive(false);
        postComments.SetActive(true);
        postComments.GetComponent<ForumPostComments>().Init(id);
        previousActivePage = currentActivePage;
        currentActivePage = postComments;
    }

    public void HidePostComments()
    {
        currentActivePage.SetActive(false);
        postComments.SetActive(false);
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }
    
    public void ShowCommentComments()
    {
        currentActivePage.SetActive(false);
        commentComments.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = commentComments;
    }
    
    public void HideCommentComments()
    {
        
        currentActivePage.SetActive(false);
        commentComments.SetActive(false);
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }
    
    public void ShowEditProfilePage()
    {
        currentActivePage.SetActive(false);
        editProfilePage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = editProfilePage;
    }
    
    public void ShowCreateEditPostPage()
    {
        currentActivePage.SetActive(false);
        createEditPostPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = editProfilePage;
    }
    
    public void HideCreateEditPostPage()
    {
        currentActivePage.SetActive(false);
        createEditPostPage.SetActive(false);
        previousActivePage.SetActive(true);
        gameDetailsPage.GetComponent<GameDetails>().OnClickedForumButton();
        groupDetailsPage.GetComponent<GroupDetails>().OnClickedForumButton();
        currentActivePage = previousActivePage;
    }
    
    public void ShowCharactersPage()
    {
        currentActivePage.SetActive(false);
        charactersPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = editProfilePage;
    }
    
    public void HideCharactersPage()
    {
        currentActivePage.SetActive(false);
        charactersPage.SetActive(false);
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }
    
    public void ShowCharacterDetailsPage()
    {
        currentActivePage.SetActive(false);
        characterDetailsPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = editProfilePage;
    }
    
    public void HideCharacterDetailsPage()
    {
        currentActivePage.SetActive(false);
        characterDetailsPage.SetActive(false);
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }
    
    public void ShowCreateCharacterPage()
    {
        currentActivePage.SetActive(false);
        createCharacterPage.SetActive(true);
        previousActivePage = currentActivePage;
        currentActivePage = createCharacterPage;
    }
    
    public void HideCreateCharacterPage()
    {
        currentActivePage.SetActive(false);
        previousActivePage.SetActive(true);
        currentActivePage = previousActivePage;
    }
    
}
