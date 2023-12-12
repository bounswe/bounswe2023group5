using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;
using TMPro;
using Unity.VisualScripting;
using UnityEngine.UI;

public class DeleteTag : MonoBehaviour
{
    [SerializeField] private TMP_InputField tagName;
    [SerializeField] private Button deleteTagButton;
    [SerializeField] private TMP_Text infoText;
    
    private CanvasManager canvasManager;
    private string tagType;
    private string color;
    private bool tagExists;
    private bool isQueryClicked;
    private string tagId;

    private string[] colorList = new[]
    {
        "#8ECAE6", "#219EBC", "#023047", "#FFB703", "#FB8500",
        "#845A6D", "#A1E8AF", "#F5EE9E"
    };

    private string[] tagTypeList = new[]
    {
        "ART_STYLE", "GENRE", "DURATION", "OTHER", "MONETIZATION",
        "PLATFORM", "DEVELOPER", "PLAYER_TYPE", "POST", "PRODUCTION"
    };
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        
        
        // Add listeners
        deleteTagButton.onClick.AddListener(OnClickedDeleteTag);
    }

    public void OnEnable()
    {
        // Initial values of input fields are ""
        tagName.text = "";
        
        tagExists = false;
        isQueryClicked = false;
    }
    

    public void OnClickedDeleteTag()
    {
        if (tagName.text == "")
        {
            infoText.text = "You must enter a tag name";
            infoText.color = Color.red;
            return;
        }
        else
        {
            Debug.Log("tag exists");
        }
        
        // Query the tag and check if it exists
        OnClickedQueryTag(() =>
        {
            if (!tagExists)
            {
                return;
            }
        
            string url = AppVariables.HttpServerUrl + "/tag/delete" + 
                         ListToQueryParameters.ListToQueryParams(new []{"id"}
                             , new []{tagId}); 
            StartCoroutine(Delete(url));
        });

        
    }
    IEnumerator Delete(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200 && response != "[]")
        {

            infoText.text = "Tag is deleted";
            infoText.color = Color.green;
            
            Debug.Log("Success to delete tag: " + response);
        }
        else
        {
            
            Debug.Log("Error to update tag: " + response);

            if (response == "[]")
            {
                infoText.text = "Empty array returned";
            }
            else
            {
                TagInfo _DeleteTagResponseData;

                try
                {
                    _DeleteTagResponseData = JsonConvert.DeserializeObject<TagInfo>(response);
                    infoText.text = _DeleteTagResponseData.message ;
                }
                catch (JsonReaderException e)
                {
                    Console.WriteLine(e);

                    infoText.text = response;
                }
            }
            infoText.color = Color.red;
        }
        request.downloadHandler.Dispose();
    }
    
    private void OnClickedExit()
    {
        canvasManager.HideDeleteTagPage();
    }
    
    

    public void OnClickedQueryTag(Action callback)
    {

        isQueryClicked = true;
        
        string url = AppVariables.HttpServerUrl + "/tag/get-all" +
                     ListToQueryParameters.ListToQueryParams(
                         new[] { "name" }, new[] { tagName.text });
        StartCoroutine(WaitForCoroutine(
            GetTagByName(url), callback));
    }
    
    IEnumerator WaitForCoroutine(IEnumerator routine, Action callback)
    {
        yield return StartCoroutine(routine);
        callback?.Invoke();
    }
    
    // Get the tag and initialize the input fields with current values
    // of the tag
    IEnumerator GetTagByName(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;

        // It will be set true below if tag exists
        tagExists = false;
        
        if (request.responseCode == 200 && response!= "[]")
        {
            Debug.Log("Success for get tag name: "+ response);
            var _GetAllTagsResponseData = JsonConvert.DeserializeObject<TagResponse[]>(response);
            
            // Set the tagId, which will be used in updateTag request
            tagId = _GetAllTagsResponseData[0].id;
            
            Debug.Log("Success to get tag: " + response);

            tagExists = true;
        }
        else
        {
            Debug.Log("Error to get tag: " + response);
            
            TagInfo _UpdateTagResponseData;

            try
            {
                _UpdateTagResponseData = JsonConvert.DeserializeObject<TagInfo>(response);
                infoText.text = _UpdateTagResponseData.message;
            }
            catch (JsonReaderException e)
            {
                Console.WriteLine(e);

                if (response == "[]")
                {
                    infoText.text = "No such tag exists";
                }
                else
                {
                    infoText.text = response;
                }

            }
            catch(JsonSerializationException e)
            {
                Console.WriteLine(e);

                if (response == "[]")
                {
                    infoText.text = "No such tag exists";
                }
                else
                {
                    infoText.text = response;
                }
            }

            
            infoText.color = Color.red;
        }
        request.downloadHandler.Dispose();
    }
    
}