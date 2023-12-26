using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class HomeScreen : MonoBehaviour
{
    private CanvasManager canvasManager;
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform homePageParent;
    private List<HomePage> homePages = new List<HomePage>();

    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void Start()
    {
        ListHomeContent(null);
    }

    private void ListHomeContent(HomeRequest homeRequestData)
    {
        string url = AppVariables.HttpServerUrl + "/home";

        string bodyJsonString = (homeRequestData == null) ? "" :
            JsonConvert.SerializeObject(homeRequestData);
        
        StartCoroutine(Post(url, bodyJsonString));
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
        foreach (var homePage in homePages)
        {
            Destroy(homePage.gameObject);
        }
        homePages.Clear();
        var request = new UnityWebRequest(url, "GET");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler)new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler)new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log(response);
        var _homeData = JsonConvert.DeserializeObject<HomeResponse[]>(response);
        if (request.responseCode != 200 || _homeData == null)
        {
            Debug.Log("error");
        }
        else
        {
            foreach (var homeData in _homeData)
            {
                HomePage newHomePage = Instantiate(Resources.Load<HomePage>("Prefabs/HomePage"), homePageParent);
                homePages.Add(newHomePage);
                newHomePage.Init(homeData);
            }
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
