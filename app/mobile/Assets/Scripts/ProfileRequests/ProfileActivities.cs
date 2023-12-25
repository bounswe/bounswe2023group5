using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public class ProfileActivities : MonoBehaviour
{
    [SerializeField] private GameObject activityParent;
    [SerializeField] private GameObject activityPrefab;

    private void Start()
    {
        Init();
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + "/profile/get-activites";
        StartCoroutine(Get(url));
    }
    
    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to get profile activities: " + response);
            var profileActivities = JsonConvert.DeserializeObject<ProfileActivity[]>(response);
            foreach (var activityData in profileActivities)
            {
                Instantiate(activityPrefab, activityParent.transform).GetComponent<RecentActivities>().Init(activityData.description, activityData.type);
            }
        }
        else
        {
            Debug.Log("Error to get profile activities: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
}