using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
// using UnityEngine.Networking;

using System.Net.Http;
using UnityEngine.UI;

public class GetRequestDemo : MonoBehaviour
{
    private TMP_InputField outputArea;
    [SerializeField] private Button getDataButton;
    
    void Start()
    {
        outputArea = GetComponent<TMP_InputField>();
        getDataButton.onClick.AddListener(GetData);
    }

    async void GetData()
    {
        outputArea.text = "Loading...";
        // string url = "https://jsonplaceholder.typicode.com/posts/1";
        string url = "https://www.freetogame.com/api/games?category=shooter&platform=browser";
        using (var httpClient = new HttpClient())
        {
            var response = await httpClient.GetAsync(url);
            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                outputArea.text = responseContent;
            }
            else
            {
                outputArea.text = response.StatusCode.ToString();
            }
        }
    }
}
