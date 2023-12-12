using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class ForumCreatePost : MonoBehaviour
{
    [SerializeField] private string title;
    [SerializeField] private string postContent;
    [SerializeField] private string postImage;
    [SerializeField] private string forumID;
    [SerializeField] private string[] tags;

    private void Start()
    {
        Init("b4036d6f-0e69-4df3-a935-a84750dc2bcd");
    }

    public void Init(string _forumID)
    {
        forumID = _forumID;
        string url = AppVariables.HttpServerUrl + "/post/create";
        var postCreateRequest = new PostCreateRequest();
        postCreateRequest.title = title;
        postCreateRequest.postContent = postContent;
        postCreateRequest.postImage = postImage;
        postCreateRequest.forum = forumID;
        postCreateRequest.tags = tags;
        string bodyJsonString = JsonUtility.ToJson(postCreateRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
