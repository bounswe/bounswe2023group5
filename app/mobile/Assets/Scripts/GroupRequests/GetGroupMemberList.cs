using System;
using System.Collections;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GetGroupMemberList : MonoBehaviour
{

    private CanvasManager canvasManager;
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform memberBoxParent;
    private List<MemberBox> memberList = new List<MemberBox>();

    private string[] moderators;
    private string[] members;
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnEnable()
    {
        // ListForumPosts("b4036d6f-0e69-4df3-a935-a84750dc2bcd");
    }


    public void GetMemberList(string[] pars, string[] vals)
    {

        string url =
            $"{AppVariables.HttpServerUrl}/group/get" +
                ListToQueryParameters.ListToQueryParams(pars, vals);
        
        Debug.Log(url);
        
        // Get the members list here
        var GotMembers = Task.Run(() => GetMembers(url)).GetAwaiter().GetResult();

        if (GotMembers)
        {
            string baseUrl = $"{AppVariables.HttpServerUrl}/user/get-all";
            
            // Then for each member, get user and populate the member box
            WriteMembersToBox(baseUrl);
        }
    }

    private async Task<bool> GetMembers(string url)
    {
        using (HttpClient client = new HttpClient())
        {
            try
            {
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    var groupData =
                        JsonConvert.DeserializeObject<GroupResponse>(await response.Content.ReadAsStringAsync());
                    members = groupData.members;
                    moderators = groupData.moderators;
                    return true;
                }
                else
                {
                    Debug.Log("Unsuccessful response. Response: " + response);
                    return false;
                }
            }
            catch (HttpRequestException e)
            {
                Debug.Log("Http request exception is encountered in GetMembers" + e);
                return false;
            }
        }
    }

    // Url with id parameter will be given 
    private async Task<GroupMember> GetUser(string url)
    {
        using (HttpClient client = new HttpClient())
        {
            try
            {
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    Debug.Log("Response at GetGroupMemberList line 95: "+ response);
                    string responseContent = await response.Content.ReadAsStringAsync();
                    Debug.Log("Response Content at GetGroupMemberList line 95: "+ responseContent);
                    var userData =
                        JsonConvert.DeserializeObject<User[]>(responseContent);
                    GroupMember groupMember = new GroupMember();
                    groupMember.userName = userData[0].username;
                    groupMember.userImage = "";
                    return groupMember;
                }
                else
                {
                    Debug.Log("Unsuccessful response. Response: " + response);
                    return null;
                }
            }
            catch (HttpRequestException e)
            {
                Debug.Log("Http request exception is encountered in GetMembers" + e);
                return null;
            }
        }
    }
    
    void WriteMembersToBox(string url)
    {
        foreach (var memberBox in memberList)
        {
            Destroy(memberBox.gameObject);
        }
        memberList.Clear();
        
        /*
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        */
        Debug.Log("Url: " + url);
        // var _forumData = JsonConvert.DeserializeObject<GetPostListResponse[]>(response);
        
        
        foreach (var memberId in members)
        {
            var groupMember = Task.Run(() => GetUser(
                url + ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{memberId})
            )).GetAwaiter().GetResult();

            if (groupMember != null  && groupMember.userName != "")
            {
                MemberBox newMemberBox = Instantiate(Resources.Load<MemberBox>("Prefabs/MemberBox"), memberBoxParent);
                memberList.Add(newMemberBox);
                newMemberBox.Init(groupMember);
            }
            
        }
        Canvas.ForceUpdateCanvases();
        scrollRect.verticalNormalizedPosition = 1;
        Debug.Log("Success to list members");
        
    }

}
