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
    [SerializeField] private TMP_Text userEmailText;
    [SerializeField] private Image userAvatarImage;
    
    [SerializeField] private Button steamProfile;
    [SerializeField] private Button epikProfile;
    [SerializeField] private Button xBoxProfile;
    
    private string userId ;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();
    
    [SerializeField] private Button adminPanelButton;
    private string role;
    private CanvasManager canvasManager;
    
    [SerializeField] private GameObject achievementParent;
    [SerializeField] private GameObject achievementPrefab;
    [SerializeField] private GameObject gamePageContent;
    [SerializeField] private GameObject groupPageContent;
    [SerializeField] private GameObject notificationPageContent;
    
    [SerializeField] private GameObject gameSection;
    [SerializeField] private GameObject groupSection;
    [SerializeField] private GameObject notificationSection;
    [SerializeField] private GameObject recentActivitiesSection;
    
    private List<GameObject> allObjects = new List<GameObject>();
    private GetProfileResponse profileResponseData;
    private void OnEnable()
    {
        queryParams.Clear();
        queryParams.Add("userId", PersistenceManager.id);
        Init();
    }

    private void Awake()
    {
        steamProfile.onClick.AddListener(OnClickedSteamProfile);
        epikProfile.onClick.AddListener(OnClickedEpikProfile);
        xBoxProfile.onClick.AddListener(OnClickedXBoxProfile); 
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
            userEmailText.text = profileResponseData.user.email;
            role = profileResponseData.user.role;
            SetProfilButton();
            string profilUrl = AppVariables.HttpImageUrl + profileResponseData.profilePhoto;
      
            steamProfile.gameObject.SetActive(profileResponseData.steamProfile != null);
            epikProfile.gameObject.SetActive(profileResponseData.epicGamesProfile != null);
            xBoxProfile.gameObject.SetActive(profileResponseData.xboxProfile != null);
            
            foreach (var achievement in profileResponseData.achievements)
            {
                GameObject achievementObject = Instantiate(achievementPrefab, achievementParent.transform);
                achievementObject.GetComponent<AchievementPopUp>().Init(achievement);
                allObjects.Add(achievementObject);
            }

            if (!profilUrl.Contains("webp"))
            {
                StartCoroutine(LoadImageFromURL(profilUrl, userAvatarImage));
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
        if (imageUrl.Contains("webp"))
        {
            yield return null;
        } 
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
            // StartCoroutine(UploadSprite(texture2, "profile"));
        }
    } 
    
    

    
    private GameObject lastObject;
    private List<GameObject> gamePages = new List<GameObject>();
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
        ProfileItem gamePagePrefab = Resources.Load<ProfileItem>("Prefabs/ProfileItem");
        if (profileResponseData.games==null)
        {
            return;
        }
        foreach (var gameData in profileResponseData.games)
        {
  
            ProfileItem gamePageObject = Instantiate(gamePagePrefab, gamePageContent.transform);
            gamePageObject.InitGamesPage(gameData.gameName, gameData.id);
            gamePages.Add(gamePageObject.gameObject);
        }
    }
    private List<GameObject> groupPages = new List<GameObject>();
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
        ProfileItem groupPagePrefab = Resources.Load<ProfileItem>("Prefabs/ProfileItem");
        if (profileResponseData.groups==null)
        {
            return;
        }
        foreach (var gameData in profileResponseData.groups)
        {
            ProfileItem groupPageObject = Instantiate(groupPagePrefab, groupPageContent.transform);
            groupPageObject.InitGroupPage(gameData.title, gameData.id);
            groupPages.Add(groupPageObject.gameObject);
        }
    }
    public void ShowMyNotifications()
    {
        queryParams.Clear();
        if (lastObject != null)
        {
            lastObject.SetActive(false);
        }
        notificationSection.SetActive(true);
        lastObject = notificationSection;
        queryParams.Add("isRead ", "true");
        string url = AppVariables.HttpServerUrl + "/notification/get-notifications" +                      
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
        StartCoroutine(GetNotification(url));
    }
    
    private List<GameObject> notificationPages = new List<GameObject>();
    IEnumerator GetNotification(string url)
    {
        notificationPages.ForEach(Destroy);
        notificationPages.Clear();
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get notifications: " + response);
            
            var notificationResponseData = JsonConvert.DeserializeObject<NotificationResponse[]>(response);
            ProfileItem notificationPrefab = Resources.Load<ProfileItem>("Prefabs/ProfileItem");
            foreach (var notification in notificationResponseData)
            {
                ProfileItem notificationPageObject = Instantiate(notificationPrefab, notificationPageContent.transform);
                notificationPageObject.InitNotificationPage(notification.message);
                notificationPages.Add(notificationPageObject.gameObject);
            }
            
        }
        else
        {
            Debug.Log("Error to get notifications: " + response);
        }
        request.downloadHandler.Dispose();
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
    
    private void OnClickedSteamProfile()
    {
        Application.OpenURL(profileResponseData.steamProfile);
    }
    
    private void OnClickedEpikProfile()
    {
        Application.OpenURL(profileResponseData.epicGamesProfile);
    }
    
    private void OnClickedXBoxProfile()
    {
        Application.OpenURL(profileResponseData.xboxProfile);
    }
    
    
}

public class NotificationResponse
{
    public string id;
    public DateTime createdAt;
    public bool isDeleted;
    public string parent;
    public string parentType;
    public string message;
    public string user;
    public bool isRead;
}
