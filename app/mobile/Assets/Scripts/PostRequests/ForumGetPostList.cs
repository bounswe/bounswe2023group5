using System;
using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class ForumGetPostList : MonoBehaviour
{

    private CanvasManager canvasManager;
    [SerializeField] private ScrollRect scrollRect;
    [SerializeField] private Transform forumPageParent;
    private List<ForumPost> forumPosts = new List<ForumPost>();
    [SerializeField] private ForumPostComments commentManager;
    [SerializeField] private ForumCreatePost editPostManager;
    //[SerializeField] private CommentComments lcommentManager;
    
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        // editPostManager = FindObjectOfType(typeof(ForumCreatePost)) as ForumCreatePost;
    }

    private void OnEnable()
    {
        // ListForumPosts("b4036d6f-0e69-4df3-a935-a84750dc2bcd");
    }


    public void ListForumPosts(string[] pars, string[] vals, ForumCreatePost forumCreatePostManager)
    {
        editPostManager = forumCreatePostManager;

        string url =
            $"{AppVariables.HttpServerUrl}/post/get-post-list" +
                ListToQueryParameters.ListToQueryParams(pars, vals);
        
        Debug.Log(url);
        /*
        string url = AppVariables.HttpServerUrl + "/post/get-post-list?forum="
                                                + forumId +"&sortBy=CREATION_DATE"+
                                                "&sortDirection=ASCENDING";
        */
        StartCoroutine(Get(url));
    }

    IEnumerator Get(string url)
    {
        foreach (var forumPost in forumPosts)
        {
            Destroy(forumPost.gameObject);
        }
        forumPosts.Clear();
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        Debug.Log("Url: " + url);
        Debug.Log("Response: " + response);
        var _forumData = JsonConvert.DeserializeObject<GetPostListResponse[]>(response);
        if (request.responseCode != 200 || _forumData == null)
        {
            Debug.Log("Error to list forum post: " + response);
        }
        else
        {
            foreach (var postData in _forumData)
            {
                ForumPost newForumPost = Instantiate(Resources.Load<ForumPost>("Prefabs/ForumPostNew"), forumPageParent);
                forumPosts.Add(newForumPost);
                if (commentManager == null)
                {
                    Debug.Log("comment manager is null");
                }
                else
                {
                    Debug.Log("comment manager is not null");
                }
                
                if (editPostManager == null)
                {
                    Debug.Log("edit post manager is null");
                }
                else
                {
                    Debug.Log("edit post manager is not null");
                }
                
                newForumPost.Init(postData, commentManager, editPostManager);
            }
            Canvas.ForceUpdateCanvases();
            scrollRect.verticalNormalizedPosition = 1;
            Debug.Log("Success to list forum post");
        }
    }

}
