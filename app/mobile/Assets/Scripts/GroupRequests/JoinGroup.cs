using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class JoinGroup : MonoBehaviour
{
    private string groupId;

    private void Start()
    {
        Init(new[] {"id"},new[] {""});

    }

    public void Init(string[] pars, string[] vals)
    {
        groupId  = ListToQueryParameters.GetValueOfParam(
            pars, vals, "id" );

        if (groupId == "")
        {
            Debug.Log("groupId must be specified");
        }

        string url = AppVariables.HttpServerUrl + "/group/join" + 
                     ListToQueryParameters.ListToQueryParams(pars, vals);
        StartCoroutine(POST(url));
    }
    
    IEnumerator POST(string url)
    {
        var request = new UnityWebRequest(url, "POST");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to join group: " + response);
        }
        else
        {
            Debug.Log("Error to join group: " + response);
        }
        request.downloadHandler.Dispose();
    }
}
