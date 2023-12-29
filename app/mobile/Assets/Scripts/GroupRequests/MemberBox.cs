using System;
using System.Collections;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class MemberBox : MonoBehaviour
{
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text userName;
    [SerializeField] private Button banUser;
    [SerializeField] private Button unbanUser;
    
    private CanvasManager canvasManager;
    private string userId;
    private string groupId;

    private void Awake()
    {
       //  gameDetailsButton.onClick.AddListener(OnClickedGameDetailsButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        banUser.onClick.AddListener(doBanUser);
        unbanUser.onClick.AddListener(doUnbanUser);
    }

    public void Init(GroupMember memberInfo, bool isMemberModerator, bool isUserModerator, bool isBanned, string gid)
    {
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/";
        // StartCoroutine(LoadImageFromURL(url + gameInfo.gameIcon, gameImage));
        // poster.text = postInfo.poster;
        userName.text = memberInfo.userName;
        groupId = gid;
        userId = memberInfo.id;
        
        if (!isUserModerator)
        {
            banUser.gameObject.SetActive(false);
            unbanUser.gameObject.SetActive(false);
        }
        
        if (isMemberModerator)
        {
            userName.text += "(moderator)";
            banUser.gameObject.SetActive(false);
            unbanUser.gameObject.SetActive(false);
        }
        else
        {
            
            if (isBanned)
            {
                banUser.gameObject.SetActive(false);
            }
            else
            {
                unbanUser.gameObject.SetActive(false);
            }
            
        }
        

        // If user image functionality will be added, it can be connected here
    }

    private void doBanUser()
    {
        string url = AppVariables.HttpServerUrl + "/group/ban-user" +
                     ListToQueryParameters.ListToQueryParams(
                         new[]{"groupId", "userId"},
                         new []{groupId, userId});
        
        StartCoroutine(PutBanUser(url));


        // Delete this user from user list

    }

    IEnumerator PutBanUser(string url)
    {
        var request = new UnityWebRequest(url, "Put");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {

            Debug.Log("Success to ban user: " + response);

            // Remove the user from the list
        }
        else
        {
            Debug.Log("Error to ban user: " + response);

        }

        request.downloadHandler.Dispose();
    }
    
    private void doUnbanUser()
    {
        string url = AppVariables.HttpServerUrl + "/group/unban-user" +
                     ListToQueryParameters.ListToQueryParams(
                         new[]{"groupId", "userId"},
                         new []{groupId, userId});
        
        StartCoroutine(PutUnbanUser(url));


        // Delete this user from user list

    }

    IEnumerator PutUnbanUser(string url)
    {
        var request = new UnityWebRequest(url, "Put");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {

            Debug.Log("Success to unban user: " + response);

            // Remove the user from the list
        }
        else
        {
            Debug.Log("Error to unban user: " + response);

        }

        request.downloadHandler.Dispose();
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