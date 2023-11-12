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
        ListForumPosts();
    }


    private void ListForumPosts()
    {
        string url = AppVariables.HttpServerUrl + "/post/get-post-list";
        var forumRequestData = new GetForumRequest();
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
        var _forumData = JsonConvert.DeserializeObject<PostListEntry[]>(response);
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
