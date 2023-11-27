using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GetAllGroups : MonoBehaviour
{
    
    private List<GroupPage> groupPages = new List<GroupPage>();
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform groupPageParent;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/group/get-all";
        StartCoroutine(GET(url));
    }
    
    IEnumerator GET(string url)
    {
        foreach (var groupPage in groupPages)
        {
            Destroy(groupPage.gameObject);
        }
        groupPages.Clear();
        
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            var _groupsData = JsonConvert.DeserializeObject<GroupGetAllResponse[]>(response);
            Debug.Log("Success to get all groups: " + response);
            foreach (var groupData in _groupsData)
            {
                GroupPage newGroupPage = Instantiate(Resources.Load<GroupPage>("Prefabs/GroupPage"), groupPageParent);
                groupPages.Add(newGroupPage);
                newGroupPage.Init(groupData);
            }
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
        }
        else
        {
            Debug.Log("Error to get all groups: " + response);
        }
        request.downloadHandler.Dispose();
    }
}
