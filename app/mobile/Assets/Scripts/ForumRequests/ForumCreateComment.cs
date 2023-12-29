using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class ForumCreateComment : MonoBehaviour
{
    private string title;
    private string postContent;
    private string forumId;
    private string[] tags;
    
    public void Init(string _forumId)
    {
        forumId = _forumId;
    }
    
    public void CreateComment(string title, string body)
    {
        string url = AppVariables.HttpServerUrl + "/comment/create";
        var forumPostData = new PostCreateRequest();
        forumPostData.title = title;
        forumPostData.postContent = postContent;
        forumPostData.forum = forumId;
        forumPostData.tags = tags;
        string bodyJsonString = JsonUtility.ToJson(forumPostData);
        StartCoroutine(CreateForumPost(url, bodyJsonString));
    }
    IEnumerator CreateForumPost(string url, string bodyJsonString)
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
        }
        else
        {
            Debug.Log("error");
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
