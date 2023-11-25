using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class GetImage : MonoBehaviour
{
    [SerializeField] private string folder;
    [SerializeField] private string fileName;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    private void Start()
    {
        queryParams.Add("folder", folder);
        queryParams.Add("fileName ", fileName);
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + $"/{folder}/{fileName}";
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
            Debug.Log("Success to get image: " + response);
        }
        else
        {
            Debug.Log("Error to get image: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}
