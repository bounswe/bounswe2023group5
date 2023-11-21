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
        canvasManager = FindObjectOfType<CanvasManager>() as CanvasManager;
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
        
        var deleteRequest = new UserDeleteRequest { id = PersistenceManager.id };
        StartCoroutine(DeleteUser(deleteRequest));
    }

    IEnumerator DeleteUser(UserDeleteRequest request)
    {
        string url = AppVariables.HttpServerUrl + "/user/delete?id=" + request.id;
        var unityWebRequest = new UnityWebRequest(url, "DELETE");
        unityWebRequest.downloadHandler = new DownloadHandlerBuffer();
        unityWebRequest.SetRequestHeader("Content-Type", "application/json");
        unityWebRequest.SetRequestHeader("Authorization", PersistenceManager.UserToken);

        yield return unityWebRequest.SendWebRequest();

        if (unityWebRequest.result != UnityWebRequest.Result.Success)
        {
            infoText.text = "Error: " + unityWebRequest.error;
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

        unityWebRequest.Dispose();
    }

    private void OnClickedBack()
    {
        canvasManager.HideDeleteAccountPage();
        canvasManager.ShowHomePage();
    }
}

