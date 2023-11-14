using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class ForumCreatePost : MonoBehaviour
{
    private string commentContent;
    private string post;
    
    
    public void CreatePost(string title, string body)
    {
        string url = AppVariables.HttpServerUrl + "/post/create";
        var commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.commentContent = commentContent;
        commentCreateRequest.post = post;
        string bodyJsonString = JsonUtility.ToJson(commentCreateRequest);
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
            Debug.Log(response);
        }
        else
        {
            Debug.Log("error");
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}
