using System;
using System.Collections;
using System.Linq;
using System.Text;
using JetBrains.Annotations;
using UnityEngine;
using TMPro;
using Unity.VisualScripting;
using UnityEngine.UI;
using UnityEngine.Networking;

public class GroupPage : MonoBehaviour
{
    [SerializeField] private Image image;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text describtion;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text dateText;
    [SerializeField] private TMP_Text members;
    [SerializeField] private Button groupDetailsButton;
    [SerializeField] private Button joinButton;
    [SerializeField] private Button leaveButton;
    [SerializeField] private Button applyButton;

    private string groupId;
    
    private CanvasManager canvasManager;


    private void Awake()
    {
        groupDetailsButton.onClick.AddListener(OnClickedGroupDetailsButton);
        joinButton.onClick.AddListener(OnClickedJoinButton);
        leaveButton.onClick.AddListener(OnClickedLeaveButton);
        applyButton.onClick.AddListener(OnClickedApplyButton);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    public void Init(GroupGetAllResponse groupInfo)
    {
        title.text = groupInfo.title;
        describtion.text = groupInfo.description;
        //tags.text = groupInfo.tags;
        string date = groupInfo.createdAt;

        dateText.text = date == null ? "" : "since " + date.Substring(8, 2) + "/" + date.Substring(5, 2) + "/" + date.Substring(0, 4);
        members.text = groupInfo.members.Length.ToString();
        groupId = groupInfo.id;
        if (groupInfo.userJoined)
        {
            joinButton.gameObject.SetActive(false);
            applyButton.gameObject.SetActive(false);
            leaveButton.gameObject.SetActive(true);
        }
        else if (groupInfo.membershipPolicy == "PRIVATE")
        {
            applyButton.gameObject.SetActive(true);
            joinButton.gameObject.SetActive(false);
            leaveButton.gameObject.SetActive(false);
        }
        else
        {
            joinButton.gameObject.SetActive(true);
            leaveButton.gameObject.SetActive(false);
            applyButton.gameObject.SetActive(false);
        }
    }
    
    
    private void OnClickedGroupDetailsButton()
    {
        // Debug.Log("Group Details Button Clicked");
        canvasManager.ShowGroupDetailsPage(groupId);
    }
    
    private void OnClickedJoinButton()
    {
        StartCoroutine(JoinGroup());
    }
    
    private void OnClickedLeaveButton()
    {
        StartCoroutine(LeaveGroup());
    }
    
    private void OnClickedApplyButton()
    {
        StartCoroutine(ApplyGroup());
    }
    
    private IEnumerator JoinGroup()
    {
        joinButton.gameObject.SetActive(false);
        string url = AppVariables.HttpServerUrl + "/group/join?id=" + groupId;
        var request = new UnityWebRequest(url, "POST");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to join group: " + response);
            joinButton.gameObject.SetActive(false);
            leaveButton.gameObject.SetActive(true);
        }
        else
        {
            Debug.Log("Error to join group: " + response);
            joinButton.gameObject.SetActive(true);
        }
        request.downloadHandler.Dispose();
    }
    
    private IEnumerator LeaveGroup()
    {
        leaveButton.gameObject.SetActive(false);
        string url = AppVariables.HttpServerUrl + "/group/leave?id=" + groupId;
        var request = new UnityWebRequest(url, "POST");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to leave group: " + response);
            joinButton.gameObject.SetActive(true);
            leaveButton.gameObject.SetActive(false);
        }
        else
        {
            Debug.Log("Error to leave group: " + response);
            leaveButton.gameObject.SetActive(true);
        }
        request.downloadHandler.Dispose();
    }
    
    private IEnumerator ApplyGroup()
    {
        applyButton.gameObject.SetActive(false);
        string url = AppVariables.HttpServerUrl + "/group/apply?id=" + groupId;
        var request = new UnityWebRequest(url, "POST");
        request.downloadHandler = new DownloadHandlerBuffer();
        var body = new GroupApplyRequest();
        body.message = "I want to join this group";
        string bodyJsonString = JsonUtility.ToJson(body);
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to apply group: " + response);
            applyButton.gameObject.SetActive(false);
        }
        else
        {
            Debug.Log("Error to apply group: " + response);
            applyButton.gameObject.SetActive(true);
        }
        request.downloadHandler.Dispose();
    }
}

public class GroupApplyRequest
{
    public string message;
}