using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GetAllGroups : MonoBehaviour
{
    
    private List<GroupPage> groupPages = new List<GroupPage>();
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform groupPageParent;
    [SerializeField] private TMP_Dropdown privacyFilter;
    [SerializeField] private TMP_Dropdown gameFilter;
    [SerializeField] private TMP_Dropdown tagFilter;
    [SerializeField] private TMP_InputField search;
    [SerializeField] private Button filterButton;
    

    private void Start()
    {
        Init();
    }
    
    

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/group/get-all";
        StartCoroutine(GET(url));
        StartCoroutine(GetAllTags(AppVariables.HttpServerUrl + "/tag/get-all"));
        StartCoroutine(GetGamesList(AppVariables.HttpServerUrl + "/game/get-game-list"));
        privacyArray.Add(("All",""));
        privacyArray.Add(("Public","PUBLIC"));
        privacyArray.Add(("Private","PRIVATE"));
        PopulateDropdown(privacyFilter, privacyArray.ConvertAll(x => x.Item1));
        filterButton.onClick.AddListener(OnClickedFilter);
    }
    

    
    private void OnClickedFilter()
    {
        var queryParams = new Dictionary<string, string>();
        if (privacyFilter.value != 0)
        {
            queryParams.Add("membershipPolicy", privacyArray[privacyFilter.value].Item2);
        }
        if (gameFilter.value != 0)
        {
            queryParams.Add("gameName", gameNamesArray[gameFilter.value].Item2);
        }
        if (tagFilter.value != 0)
        {
            queryParams.Add("tags", tagArray[tagFilter.value].Item2);
        }
        if (search.text != "")
        {
            queryParams.Add("title", search.text);
        }
        string url = AppVariables.HttpServerUrl + "/group/get-all" +                      
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
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
    
    IEnumerator GetAllTags(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            var allTagsResponseData = JsonConvert.DeserializeObject<TagResponse[]>(response);
            // Do things with _GetAllTagsResponseData 
            tagArray.Add(("Choose Tag",""));
            foreach (var tagResponse in allTagsResponseData)
            {
                if (tagResponse.tagType == "GROUP")
                {
                    tagArray.Add((tagResponse.name,tagResponse.id));
                }
            }
            Debug.Log("Success to get tags: " + response);
        }
        else
        {
            Debug.Log("Error to get tags: " + response);
        }
        request.downloadHandler.Dispose();
        PopulateDropdown(tagFilter, tagArray.ConvertAll(x => x.Item1));
    }
    
    IEnumerator GetGamesList(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log(response);
        var _gamesData = JsonConvert.DeserializeObject<GameListEntry[]>(response);
        if (request.responseCode != 200 || _gamesData == null)
        {
            Debug.Log("error");
        }
        else
        {
            gameNamesArray.Add(("Choose Game",""));
            foreach (var gameElement in _gamesData)
            {
                gameNamesArray.Add((gameElement.gameName,gameElement.gameName));
            }
        }
        request.downloadHandler.Dispose();
        PopulateDropdown(gameFilter, gameNamesArray.ConvertAll(x => x.Item1));
    }
    
    private List<(string,string)> gameNamesArray = new List<(string,string)>();
    private List<(string,string)> privacyArray = new List<(string,string)>();
    private List<(string,string)> tagArray = new List<(string,string)>();
    void PopulateDropdown (TMP_Dropdown dropdown, List<string> options) {
        dropdown.ClearOptions ();
        dropdown.AddOptions(options);
    }
}
