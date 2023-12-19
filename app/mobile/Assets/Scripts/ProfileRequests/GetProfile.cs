using System;
using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.Serialization;
using UnityEngine.UI;

public class GetProfile : MonoBehaviour
{
    [SerializeField] private TMP_Text usernameText;
    [SerializeField] private TMP_Text userTypeText;
    [SerializeField] private Image userAvatarImage;
    
    [SerializeField] private GameObject steamProfile;
    [SerializeField] private GameObject epikProfile;
    [SerializeField] private GameObject xBoxProfile;
    
    private string userId ;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();
    
    [SerializeField] private Button adminPanelButton;
    private string role;
    private CanvasManager canvasManager;
    
    [SerializeField] private GameObject achievementParent;
    [SerializeField] private GameObject achievementPrefab;
    [SerializeField] private GameObject gamePageContent;
    [SerializeField] private GameObject groupPageContent;
    
    [SerializeField] private GameObject gameSection;
    [SerializeField] private GameObject groupSection;
    [SerializeField] private GameObject reviewSection;
    [SerializeField] private GameObject recentActivitiesSection;
    
    private List<GameObject> allObjects = new List<GameObject>();
    private GetProfileResponse profileResponseData;
    private void OnEnable()
    {
        queryParams.Clear();
        queryParams.Add("userId", PersistenceManager.id);
        Init();
    }
    

    public void Init()
    { 
        string url = AppVariables.HttpServerUrl + "/profile/get" +                      
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
        StartCoroutine(Get(url));
    }
    
    IEnumerator Get(string url)
    {
        allObjects.ForEach(Destroy);
        allObjects.Clear();
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get profile: " + response);
            profileResponseData = JsonConvert.DeserializeObject<GetProfileResponse>(response);
            PersistenceManager.ProfileId = profileResponseData.id;
            usernameText.text = profileResponseData.user.username;
            userTypeText.text = profileResponseData.user.role;
            role = profileResponseData.user.role;
            SetProfilButton();
            StartCoroutine(LoadImageFromURL(AppVariables.HttpImageUrl+profileResponseData.profilePhoto, userAvatarImage));
            if (profileResponseData.steamProfile != null)
            {
                steamProfile.SetActive(true);
            }
            if (profileResponseData.epicGamesProfile != null)
            {
                epikProfile.SetActive(true);
            }
            if (profileResponseData.xboxProfile != null)
            {
                xBoxProfile.SetActive(true);
            }

            foreach (var achievement in profileResponseData.achievements)
            {
                GameObject achievementObject = Instantiate(achievementPrefab, achievementParent.transform);
                achievementObject.GetComponent<AchievementPopUp>().Init(achievement);
                allObjects.Add(achievementObject);
            }
            
        }
        else
        {
            Debug.Log("Error to get profile: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
    private void SetProfilButton()
    {
        // If she is admin, make Admin Control Panel button visible
        // and add listener for clicking
        if (role == "ADMIN")
        {
            Debug.Log("Role: "+ PersistenceManager.Role);
            adminPanelButton.gameObject.SetActive(true);
            canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        }
        // Otherwise make it invisible
        else
        {
            Debug.Log("Role: "+ PersistenceManager.Role);
            adminPanelButton.gameObject.SetActive(false);
            
        }
    }

    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {   
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(imageUrl);
        yield return request.SendWebRequest();
        if (request.isNetworkError || request.isHttpError)
        {
            Debug.Log(request.error);
        }
        else
        {
            Texture2D texture2;
            texture2 = ((DownloadHandlerTexture) request.downloadHandler).texture;
            Sprite sprite = Sprite.Create(texture2, new Rect(0, 0, texture2.width, texture2.height), new Vector2(0, 0));
            targetImage.sprite = sprite;
        }
    } 
    
    private GameObject lastObject;
    private List<GamePage> gamePages = new List<GamePage>();
    public void ShowMyGames()
    {
        gamePages.ForEach(Destroy);
        gamePages.Clear();
        if (lastObject != null)
        {
            lastObject.SetActive(false);
        }
        gameSection.SetActive(true);
        lastObject = gameSection;
        GamePage gamePagePrefab = Resources.Load<GamePage>("Prefabs/GamePage");
        foreach (var gameData in profileResponseData.games)
        {
            GameListEntry gameListEntry = new GameListEntry();
            gameListEntry.id = gameData.id;
            gameListEntry.gameName = gameData.gameName;
            gameListEntry.gameDescription = gameData.gameDescription;
            gameListEntry.gameIcon = gameData.gameIcon;
            GamePage gamePageObject = Instantiate(gamePagePrefab, gamePageContent.transform);
            gamePageObject.Init(gameListEntry);
            gamePages.Add(gamePageObject);
        }
    }
    private List<GroupPage> groupPages = new List<GroupPage>();
    public void ShowMyGroups()
    {
        groupPages.ForEach(Destroy);
        groupPages.Clear();
        if (lastObject != null)
        {
            lastObject.SetActive(false);
        }
        groupSection.SetActive(true);
        lastObject = groupSection;
        GroupPage groupPagePrefab = Resources.Load<GroupPage>("Prefabs/GroupPage");
        foreach (var gameData in profileResponseData.groups)
        {
            GroupGetAllResponse gameListEntry = new GroupGetAllResponse();
            gameListEntry.id = gameData.id;
            gameListEntry.title = gameData.title;
            gameListEntry.description = gameData.description;
            gameListEntry.membershipPolicy = gameData.membershipPolicy;
            gameListEntry.quota = gameData.quota;
            gameListEntry.members = gameData.members;
            GroupPage groupPageObject = Instantiate(groupPagePrefab, gamePageContent.transform);
            groupPageObject.Init(gameListEntry);
            groupPages.Add(groupPageObject);
        }
    }
    public void ShowMyReviews()
    {
        if (lastObject != null)
        {
            lastObject.SetActive(false);
        }
        reviewSection.SetActive(true);
        lastObject = reviewSection;
    }
    public void ShowRecentActivities()
    {
        if (lastObject != null)
        {
            lastObject.SetActive(false);
        }
        recentActivitiesSection.SetActive(true);
        lastObject = recentActivitiesSection;
    }
    
}
