using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class RemoveGroupModerator : MonoBehaviour
{
    private string groupId;
    private string userId;

    private void Start()
    {
        Init(new[] {"groupId","userId"},new[] {"",""});

    }

    public void Init(string[] pars, string[] vals)
    {
        groupId  = ListToQueryParameters.GetValueOfParam(
            pars, vals, "groupId" );
        userId  = ListToQueryParameters.GetValueOfParam(
            pars, vals, "userId" );
        
        if (groupId == "" || userId == "")
        {
            Debug.Log("groupId and userId must be specified");
        }

        string url = AppVariables.HttpServerUrl + "/group/remove-moderator" + 
                     ListToQueryParameters.ListToQueryParams(pars, vals);
        StartCoroutine(PUT(url));
    }
    
    IEnumerator PUT(string url)
    {
        var request = new UnityWebRequest(url, "PUT");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to remove group moderator: " + response);
        }
        else
        {
            Debug.Log("Error to remove group moderator: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}
