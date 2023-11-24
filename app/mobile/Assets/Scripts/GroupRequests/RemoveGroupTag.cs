using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.Networking;

public class RemoveGroupTag : MonoBehaviour
{
    [SerializeField] private string groupId;
    [SerializeField] private string TagId;

    private void Awake()
    {
        GetComponent<Button>().onClick.AddListener(DoRemoveTag);
    }

    public void DoRemoveTag()
    {
        string url = AppVariables.HttpServerUrl + "/group/remove-tag";
        var groupRemoveTagRequest = new GroupRemoveTagRequest();
        groupRemoveTagRequest.groupId = groupId;
        groupRemoveTagRequest.tagId = TagId;
        string bodyJsonString = JsonUtility.ToJson(groupRemoveTagRequest);
        StartCoroutine(DELETE(url, bodyJsonString));
    }
    
    IEnumerator DELETE(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "DELETE");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to delete group tag: " + response);
        }
        else
        {
            Debug.Log("Error to delete group tag: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
}
