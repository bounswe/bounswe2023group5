using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

namespace NotificationRequests
{
    public class GetNotification : MonoBehaviour
    {
        private Dictionary<string,string> queryParams = new Dictionary<string, string>();

        private void Start()
        {
            queryParams.Add("isRead ", "true");
            Init();
        }

        public void Init()
        { 
            string url = AppVariables.HttpServerUrl + "/notification/get-notifications" +                      
                         DictionaryToQueryParameters.DictionaryToQuery(queryParams);
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
                Debug.Log("Success to get notifications: " + response);
            }
            else
            {
                Debug.Log("Error to get notifications: " + response);
            }
            request.downloadHandler.Dispose();
        }
    }
}