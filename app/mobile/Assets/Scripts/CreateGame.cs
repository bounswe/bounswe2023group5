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
        var createData = new CreateGameRequest();
        //createData.email = emailInputField.text;
        //createData.password = passwordInputField.text;
        string bodyJsonString = JsonConvert.SerializeObject(createData);
        StartCoroutine(Post(url, bodyJsonString));
       
    }
    
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = (UploadHandler) new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        var _CreateGameResponseData = JsonConvert.DeserializeObject<CreateGameResponse>(response);
      
    }
    

}