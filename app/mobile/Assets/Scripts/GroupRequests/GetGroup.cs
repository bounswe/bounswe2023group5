using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class GetGroup : MonoBehaviour
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

        string url = AppVariables.HttpServerUrl + "/group/get" + 
                     ListToQueryParameters.ListToQueryParams(pars, vals);
        StartCoroutine(GET(url));
    }
    
    IEnumerator GET(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get group: " + response);
        }
        else
        {
            Debug.Log("Error to get group: " + response);
        }
        request.downloadHandler.Dispose();
    }
}
