using System;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using TMPro;
using Newtonsoft.Json;
using System.Collections;
using System.Text;
using DG.Tweening;

public class DeleteAccount : MonoBehaviour
{
    public TMP_InputField passwordInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button deleteButton;
    [SerializeField] private Button backButton;
    [SerializeField] private Toggle checkBox;
    [SerializeField] private TMP_Text infoText;

    
    private void Awake()
    {
        deleteButton.onClick.AddListener(OnClickedDelete);
        backButton.onClick.AddListener(OnClickedBack);
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedDelete()
    {
        bool isConfirmed = checkBox.isOn;

        if (PersistenceManager.Password != passwordInputField.text)
        {
            infoText.text = "Wrong password";    
            infoText.color = Color.red;
            return;
        }

        if (!isConfirmed)
        {
            infoText.text = "You have to confirm deleting account";    
            infoText.color = Color.red;
            return;
        }
        
        string url = AppVariables.HttpServerUrl + "/user/delete";
        var deleteData = new DeleteAccountRequest();
        deleteData.id = PersistenceManager.id;
        string bodyJsonString = JsonConvert.SerializeObject(deleteData);
        StartCoroutine(Delete(url, bodyJsonString));

    }
    IEnumerator Delete(string url, string bodyJsonString)
    {
        url += "?id=" + PersistenceManager.id;
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = (DownloadHandler) new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        var response = request.downloadHandler.text;
        
        if (request.responseCode != 200 || response == null)
        {
            infoText.text = "Error: " + request.responseCode;
            infoText.color = Color.red;
        }
        else if (response == "false")
        {
            infoText.text = "Error: " + response;
            infoText.color = Color.red;
        }
        else
        {
            infoText.text = "Successfully Deleted";
            infoText.color = Color.green;
            
            DOVirtual.DelayedCall(2f, () =>
            {
                canvasManager.HideDeleteAccountPage();
                canvasManager.ShowSignUpPage();
            });
            
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }

    private void OnClickedBack()
    {
        canvasManager.HideDeleteAccountPage();
        canvasManager.ShowHomePage();
    }


}