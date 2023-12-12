using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class DeleteGameAchievement : MonoBehaviour
{
    private string id;
    private Dictionary<string, string> queryParams = new Dictionary<string, string>();

    private void Start()
    {
        queryParams.Add("id", "b73d132b-a3e3-4776-bbe6-01c2ce0d2d2c");
        Init(queryParams);
    }

    public void Init(Dictionary<string, string> queryParams)
    {
        id = DictionaryToQueryParameters.GetValueOfParam(
            queryParams, "id" );
        if (id == "")
        {
            Debug.Log("Id must be specified");
        }
        string url = AppVariables.HttpServerUrl + "/achievement/delete" + 
                     DictionaryToQueryParameters.DictionaryToQuery(queryParams);
        StartCoroutine(Delete(url));
    }

    IEnumerator Delete(string url)
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
            Debug.Log("Success to delete game achievement: " + response);
        }
        else
        {
            Debug.Log("Error to delete game achievement: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}
