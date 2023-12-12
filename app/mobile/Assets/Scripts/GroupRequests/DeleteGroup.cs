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


public class DeleteGroup : MonoBehaviour
{
    
    // [SerializeField] private TMP_Text infoText;
    
    // User will be banned via the back-end
    // [SerializeField] private GameObject userList;
    
    // After the log out message is sent, the user should be navigated to 
    // another page
    private CanvasManager canvasManager;

    // There must be a way to fetch these from the environment
    private string identifier;
    
    private void Awake()
    {
        GetComponent<Button>().onClick.AddListener(DoDeleteGroup);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void DoDeleteGroup()
    {
        string url = AppVariables.HttpServerUrl + "/group/delete" + 
                     ListToQueryParameters.ListToQueryParams(
                         new []{"identifier"}, new []{identifier});
        StartCoroutine(DELETE(url));
        

    }
    
    IEnumerator DELETE(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to delete group: " + response);
        }
        else
        {
            Debug.Log("Error to delete group: " + response);
        }
        request.downloadHandler.Dispose();
    }
}

