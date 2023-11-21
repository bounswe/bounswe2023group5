using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class GetAllGroups : MonoBehaviour
{

    private void Start()
    {
        Init(new[] {"title"},new[] {""});
    }

    public void Init(string[] pars, string[] vals)
    {
        string url = AppVariables.HttpServerUrl + "/group/get-all" + 
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
            Debug.Log("Success to get all groups: " + response);
        }
        else
        {
            Debug.Log("Error to get all groups: " + response);
        }
        request.downloadHandler.Dispose();
    }
}
