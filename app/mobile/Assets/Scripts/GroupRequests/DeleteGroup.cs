using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class DeleteGroup : MonoBehaviour
{
    private string identifier;

    private void Start()
    {
        Init(new[] {"identifier"},new[] {""});

    }

    public void Init(string[] pars, string[] vals)
    {
        identifier  = ListToQueryParameters.GetValueOfParam(
            pars, vals, "identifier" );

        if (identifier == "")
        {
            Debug.Log("identifier must be specified");
        }

        string url = AppVariables.HttpServerUrl + "/group/delete" + 
                     ListToQueryParameters.ListToQueryParams(pars, vals);
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
