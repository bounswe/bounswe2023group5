using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
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

    private void Start()
    {
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
            var profileResponseData = JsonConvert.DeserializeObject<GetProfileResponse>(response);
            usernameText.text = profileResponseData.user.username;
            userTypeText.text = profileResponseData.user.role;
            role = profileResponseData.user.role;
            SetProfilButton();
            StartCoroutine(LoadImageFromURL(profileResponseData.profilePhoto, userAvatarImage));
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
                Instantiate(achievementPrefab, achievementParent.transform).GetComponent<AchievementPopUp>().Init(achievement);
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
            adminPanelButton.onClick.AddListener(onClickOpenAdminPanel);
            canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        }
        // Otherwise make it invisible
        else
        {
            Debug.Log("Role: "+ PersistenceManager.Role);
            adminPanelButton.gameObject.SetActive(false);
            
        }
    }
    
    private void onClickOpenAdminPanel()
    {
        canvasManager.ShowAdminControlPanelPage();
    }
    
    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(imageUrl);
        yield return request.SendWebRequest();

        if(request.result != UnityWebRequest.Result.Success)
        {
            // Debug.LogError("Failed to load image: " + request.error);
        }
        else
        {
            Texture2D texture = DownloadHandlerTexture.GetContent(request);
            targetImage.sprite = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }
    }
    
}
