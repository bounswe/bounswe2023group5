using System;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using TMPro;
using Newtonsoft.Json;
using System.Collections;
using System.Text;
using DG.Tweening;

public class CreateGame : MonoBehaviour
{
    public TMP_InputField nameInputField;
    public TMP_InputField summaryInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button createButton;
    [SerializeField] private TMP_Text infoText;
    
    private void Awake()
    {
        createButton.onClick.AddListener(OnClickedCreate);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedCreate()
    {
        
        string url = AppVariables.HttpServerUrl + "/game/create";
        StartCoroutine(Post(url));
       
    }
    
    IEnumerator Post(string url)
    {
        var request = new UnityWebRequest(url, "POST");
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            var _CreateGameResponseData = JsonConvert.DeserializeObject<CreateGameResponse>(response);
            Debug.Log("Success to create review: " + response);
        }
        else
        {
            Debug.Log("Error to create review: " + response);
        }
        
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
      
    }
    

}