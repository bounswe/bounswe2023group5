using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using TMPro;
using UnityEngine.UI;

public class ForumPostDetail : MonoBehaviour
{
    private string postID;
    [SerializeField] private Image userImage;
    [SerializeField] private TMP_Text poster;
    [SerializeField] private TMP_Text title;
    [SerializeField] private TMP_Text postContent;
    [SerializeField] private TMP_Text lastEditedAt;
    [SerializeField] private TMP_Text overallVote;
    [SerializeField] private TMP_Text tags;
    [SerializeField] private TMP_Text userName;


    private void Start()
    {
        // Init(new[] {"id"},new[] {"b73d132b-a3e3-4776-bbe6-01c2ce0d2d2c"});
    }

    public void Awake()
    {
        
    }

    public void Init(string postId)
    {
        postID = postId;
        
        if (postID == "")
        {
            Debug.Log("Id must be specified");
        }
        else
        {
            Debug.Log("Id is specified "+ postID);
        }
        
        string url = AppVariables.HttpServerUrl + "/post/get-post-detail" +                      
                     ListToQueryParameters.ListToQueryParams(new []{"id"}, new []{postID});
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
            var postInfo = JsonConvert.DeserializeObject<GetPostListResponse>(response);
            title.text = postInfo.title;
            postContent.text = postInfo.postContent;
            lastEditedAt.text = postInfo.lastEditedAt;
            overallVote.text = Convert.ToString(postInfo.overallVote);
            if (postInfo.poster == null)
            {
                userName.text = "(anonymous)";
            }
            else
            {
                userName.text = postInfo.poster.username;

            }

            tags.text = "";
            foreach (var tag in postInfo.tags)
            {
                tags.text =  tags.text + tag + " ";
            }
        
            if (postInfo.isEdited)
            {
                lastEditedAt.text += " (edited)";
            }
            else
            {
                // This will be deleted
                lastEditedAt.text += " (not edited)";
            }

            Debug.Log("Success to get forum post detail: " + response);
        }
        else
        {
            Debug.Log("Error to get forum post detail: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}