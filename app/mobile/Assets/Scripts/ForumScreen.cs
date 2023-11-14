using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using DG.Tweening;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class ForumScreen : MonoBehaviour
{

    private CanvasManager canvasManager;
    [SerializeField] private Transform forumPageParent;
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnEnable()
    {
        // ListForumPosts("b4036d6f-0e69-4df3-a935-a84750dc2bcd");
    }


    public void ListForumPosts(string forumId)
    {
        string url = AppVariables.HttpServerUrl + "/post/get-post-list?forum="
                                                + forumId +"&sortBy=CREATION_DATE"+
                                                "&sortDirection=ASCENDING";
        var forumRequestData = new GetPostListRequest();
        string bodyJsonString = JsonConvert.SerializeObject(forumRequestData);
        StartCoroutine(Get(url));
    }

    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        // byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        // request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log(response);
        var _forumData = JsonConvert.DeserializeObject<GetPostListResponse[]>(response);
        if (request.responseCode != 200 || _forumData == null)
        {
            Debug.Log("error");
        }
        else
        {
            foreach (var postData in _forumData)
            {
                ForumPage newForumPage = Instantiate(Resources.Load<ForumPage>("Prefabs/ForumPage"), forumPageParent);
                newForumPage.Init(postData);
            }
        }
    }

}
