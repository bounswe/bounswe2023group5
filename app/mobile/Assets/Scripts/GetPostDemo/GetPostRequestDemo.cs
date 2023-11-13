using System.Collections;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using Newtonsoft.Json;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
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
            string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/api/auth/login";
            var loginData = new LoginRequest();
            loginData.email = "al@al.com";
            loginData.password = "123";
            string bodyJsonString = JsonConvert.SerializeObject(loginData);
            // string bodyJsonString = "{\"password\":\"123\",\"email\":\"al@al.com\"}";
            _outputArea = GetComponent<TMP_InputField>();
            getDataButton.onClick.AddListener(GetData);
            postDataButton.onClick.AddListener(PostData);
            StartCoroutine(Post( url, bodyJsonString));
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
            string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/api/auth/login";
            var postData = new Dictionary<string, string>();
            postData.Add("password", "123");
            postData.Add("email", "al@al.com");
            using (var httpClient = new HttpClient())
            {
                var response = await httpClient.PostAsync(url, new FormUrlEncodedContent(postData));
                if (response.IsSuccessStatusCode)
                {
                    var responseContent = await response.Content.ReadAsStringAsync();
                    Debug.Log(responseContent.GetType());
                    // _outputArea.text = responseContent;
                }
                else
                {
                    _outputArea.text = response.StatusCode.ToString();
                }
            }
        }
        
        IEnumerator Post(string url, string bodyJsonString)
        {
            var request = new UnityWebRequest(url, "POST");
            byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
            request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
            request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
            request.SetRequestHeader("Content-Type", "application/json");
            yield return request.SendWebRequest();
            _outputArea.text = request.downloadHandler.text;
            var _useraData = JsonConvert.DeserializeObject<LoginResponse>(request.downloadHandler.text);
            Debug.Log(_useraData.user.email + " " + _useraData.token + " " + _useraData.user.id + " " + _useraData.user.role + " " + _useraData.user.isVerified + " " + _useraData.user.username);
            Debug.Log("Status Code: " + request.responseCode);
            request.downloadHandler.Dispose();
            request.uploadHandler.Dispose();
        }
        
        IEnumerator Get(string url, string bodyJsonString)
        {
            var request = new UnityWebRequest(url, "GET");
            byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
            request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
            request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
            request.SetRequestHeader("Content-Type", "application/json");
            yield return request.SendWebRequest();
            _outputArea.text = request.downloadHandler.text;
            var _useraData = JsonConvert.DeserializeObject<LoginResponse>(request.downloadHandler.text);
            Debug.Log(_useraData.user.email + " " + _useraData.token + " " + _useraData.user.id + " " + _useraData.user.role + " " + _useraData.user.isVerified + " " + _useraData.user.username);
            Debug.Log("Status Code: " + request.responseCode);
            request.downloadHandler.Dispose();
            request.uploadHandler.Dispose();
        }
        
        IEnumerator Put(string url, string bodyJsonString)
        {
            var request = new UnityWebRequest(url, "PUT");
            
            // request.SetRequestHeader("Authorization", "Bearer " + PlayerPrefs.GetString("token"));
            byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
            request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
            request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
            request.SetRequestHeader("Content-Type", "application/json");
            yield return request.SendWebRequest();
            _outputArea.text = request.downloadHandler.text;
            var _useraData = JsonConvert.DeserializeObject<LoginResponse>(request.downloadHandler.text);
            Debug.Log(_useraData.user.email + " " + _useraData.token + " " + _useraData.user.id + " " + _useraData.user.role + " " + _useraData.user.isVerified + " " + _useraData.user.username);
            Debug.Log("Status Code: " + request.responseCode);
            request.downloadHandler.Dispose();
            request.uploadHandler.Dispose();
        }
        
        IEnumerator Delete(string url, string bodyJsonString)
        {
            var request = new UnityWebRequest(url, "DELETE");
            byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
            request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
            request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
            request.SetRequestHeader("Content-Type", "application/json");
            yield return request.SendWebRequest();
            _outputArea.text = request.downloadHandler.text;
            var _useraData = JsonConvert.DeserializeObject<LoginResponse>(request.downloadHandler.text);
            Debug.Log(_useraData.user.email + " " + _useraData.token + " " + _useraData.user.id + " " + _useraData.user.role + " " + _useraData.user.isVerified + " " + _useraData.user.username);
            Debug.Log("Status Code: " + request.responseCode);
            request.downloadHandler.Dispose();
            request.uploadHandler.Dispose();
        }
    }
}
