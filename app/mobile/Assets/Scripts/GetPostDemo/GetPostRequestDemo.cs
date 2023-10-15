using System.Collections.Generic;
using System.Net.Http;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

namespace GetPostDemo
{
    public class GetPostRequestDemo : MonoBehaviour
    {
        private TMP_InputField _outputArea;
        [SerializeField] private Button getDataButton;
        [SerializeField] private Button postDataButton;    
        void Start()
        {
            _outputArea = GetComponent<TMP_InputField>();
            getDataButton.onClick.AddListener(GetData);
            postDataButton.onClick.AddListener(PostData);
        }

        async void GetData()
        {
            _outputArea.text = "Loading...";
            string url = "https://jsonplaceholder.typicode.com/posts/1";
            // string url = "https://www.freetogame.com/api/games?category=shooter&platform=browser";
            using (var httpClient = new HttpClient())
            {
                var response = await httpClient.GetAsync(url);
                if (response.IsSuccessStatusCode)
                {
                    var responseContent = await response.Content.ReadAsStringAsync();
                    _outputArea.text = responseContent;
                }
                else
                {
                    _outputArea.text = response.StatusCode.ToString();
                }
            }
        }
    
        async void PostData()
        {
            _outputArea.text = "Loading...";
            string url = "https://jsonplaceholder.typicode.com/posts";
            var postData = new Dictionary<string, string>();
            postData.Add("Title", "Mobile Game Review App");
            using (var httpClient = new HttpClient())
            {
                var response = await httpClient.PostAsync(url, new FormUrlEncodedContent(postData));
                if (response.IsSuccessStatusCode)
                {
                    var responseContent = await response.Content.ReadAsStringAsync();
                    _outputArea.text = responseContent;
                }
                else
                {
                    _outputArea.text = response.StatusCode.ToString();
                }
            }
        }
    }
}
