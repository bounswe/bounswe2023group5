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


public class BanUser : MonoBehaviour
{
    [SerializeField] private TMP_Text infoText;
    
    // I will access the userList and remove the successfully banned user
    [SerializeField] private GameObject userList;
    
    // After the log out message is sent, the user should be navigated to 
    // another page
    private CanvasManager canvasManager;

    // There must be a way to fetch these
    private string groupId;
    private string userId;
    

    private void Awake()
    {
        GetComponent<Button>().onClick.AddListener(DoBanUser);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }


    private void DoBanUser()
    {
        string url = AppVariables.HttpServerUrl + "/group/ban-user" +
                     ListToQueryParameters.ListToQueryParams(
                         new[]{"groupId", "userId"},
                         new []{groupId, userId});
        
        StartCoroutine(PutBanUser(url));


        // Delete this user from user list

    }

    IEnumerator PutBanUser(string url)
    {
        var request = new UnityWebRequest(url, "Put");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {

            Debug.Log("Success to ban user: " + response);

            infoText.text = "Banned the user";
            infoText.color = Color.green;
            
            // Remove the user from the list
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);

            infoText.text = "Problem at banning the user";
            infoText.color = Color.red;
        }

        request.downloadHandler.Dispose();
    }
}