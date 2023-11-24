using System.Collections;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;
using static CommentController;

public class ForumPostComments : MonoBehaviour
{
    [SerializeField] private string title;
    [SerializeField] private string postContent;
    private string postID;

    private void Start()
    {
        Init(new[] {"id"},new[] {"b73d132b-a3e3-4776-bbe6-01c2ce0d2d2c"});
    }

    public void Init(string[] pars, string[] vals)
    {
        postID = ListToQueryParameters.GetValueOfParam(pars, vals, "id");
        
        if (string.IsNullOrEmpty(postID))
        {
            Debug.Log("Id must be specified");
            return;
        }
        
        string url = AppVariables.HttpServerUrl + "/post/get-post-comments" + 
                     ListToQueryParameters.ListToQueryParams(pars, vals);
        StartCoroutine(Get(url));
    }
    
    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get forum post comments: " + response);
        }
        else
        {
            Debug.Log("Error to get forum post comments: " + response);
        }
        request.downloadHandler.Dispose();
    }


    public void ReplyToComment(CommentReplyRequest commentReplyRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/reply";
        string bodyJsonString = JsonConvert.SerializeObject(commentReplyRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }

    public void EditComment(CommentEditRequest commentEditRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/edit";
        string bodyJsonString = JsonConvert.SerializeObject(commentEditRequest);
        StartCoroutine(Put(url, bodyJsonString));
    }

    public void CreateComment(CommentCreateRequest commentCreateRequest)
    {
        string url = AppVariables.HttpServerUrl + "/comment/create";
        string bodyJsonString = JsonConvert.SerializeObject(commentCreateRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }

    public void DeleteComment(string commentId)
    {
        string url = AppVariables.HttpServerUrl + "/comment/delete?id=" + commentId;
        StartCoroutine(Delete(url));
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
        HandleResponse(request);
    }

    IEnumerator Put(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "PUT");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        HandleResponse(request);
    }

    IEnumerator Delete(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        HandleResponse(request);
    }

    private void HandleResponse(UnityWebRequest request)
    {
        string response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            Debug.Log("Success: " + response);
        }
        else
        {
            Debug.LogError("Error: " + response);
        }
        request.downloadHandler.Dispose();
    }
}