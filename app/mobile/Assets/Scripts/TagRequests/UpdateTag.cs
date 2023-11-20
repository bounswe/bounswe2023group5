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

public class UpdateTag : MonoBehaviour
{
    [SerializeField] private TMP_InputField currentTagName;
    [SerializeField] private TMP_InputField newTagName;
    [SerializeField] private TMP_Dropdown tagTypeDropdown;
    [SerializeField] private TMP_Dropdown colorDropdown;
    [SerializeField] private Button updateTagButton;
    [SerializeField] private RawImage colorImage;
    [SerializeField] private TMP_Text infoText;
    [SerializeField] private Button exitButton;
    [SerializeField] private Button queryButton;
    
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
        updateTagButton.onClick.AddListener(OnClickedUpdateTag);
        queryButton.onClick.AddListener(OnClickedQueryTag);
        exitButton.onClick.AddListener(OnClickedExit);
        tagTypeDropdown.onValueChanged.AddListener(OnTagTypeValueChanged);
        colorDropdown.onValueChanged.AddListener(OnColorValueChanged);
    }

    public void OnEnable()
    {
        // Initial values of input fields are ""
        currentTagName.text = "";
        CleanFields();
        
        tagExists = false;
        isQueryClicked = false;
    }

    private void CleanFields()
    {
        // Initial values of input fields are ""
        newTagName.text = "";
        
        // Initial values of dropdown texts
        tagType = tagTypeDropdown.options[0].text;
        color = colorList[0];
        Color colorTMP;
        UnityEngine.ColorUtility.TryParseHtmlString( colorList[0],
            out colorTMP);
        colorImage.color = colorTMP;

    }

    public void OnClickedUpdateTag()
    {
        if (currentTagName.text == "")
        {
            infoText.text = "You must enter a tag name";
            infoText.color = Color.red;
            CleanFields();
            return;
        }

        if (!isQueryClicked)
        {
            infoText.text = "You must write a correct tag name and " +
                            "click the query button";
            infoText.color = Color.red;
            return;
        }

        if (!tagExists)
        {
            infoText.text = "Write an existent tag name";
            infoText.color = Color.red;
            CleanFields();
            return;
        }
        else
        {
            Debug.Log("tag exists");
        }
        
        string url = AppVariables.HttpServerUrl + "/tag/update" + 
                     ListToQueryParameters.ListToQueryParams(new []{"id"}
                     , new []{tagId});
        var updateTagRequest = new UpdateTagRequest();
        updateTagRequest.name = newTagName.text;
        updateTagRequest.tagType = tagType;
        updateTagRequest.color = color;
        string bodyJsonString = JsonUtility.ToJson(updateTagRequest);
        StartCoroutine(Put(url, bodyJsonString));
    }
    IEnumerator Put(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "PUT");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200 && response != "[]")
        {
            var _UpdateTagResponseData = JsonConvert.DeserializeObject<TagResponse>(response);

            // Do things with _UpdateTagResponseData 

            infoText.text = "Tag is updated";
            infoText.color = Color.green;
            
            Debug.Log("Success to update tag: " + response);
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
                TagInfo _UpdateTagResponseData;

                try
                {
                    _UpdateTagResponseData = JsonConvert.DeserializeObject<TagInfo>(response);
                    infoText.text = _UpdateTagResponseData.message ;
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
        request.uploadHandler.Dispose();
    }
    
    private void OnClickedExit()
    {
        canvasManager.HideUpdateTagPage();
    }
    
    private void OnTagTypeValueChanged(int index)
    {
        // Get the selected item's text
        tagType = tagTypeDropdown.options[index].text;

        // Print the selected item's text to the console
        Debug.Log("Selected Item: " + tagType);
    }
    
    private void OnColorValueChanged(int index)
    {
        // Get the selected item's text
        color = colorList[index];
        
        Color colorTMP;
        UnityEngine.ColorUtility.TryParseHtmlString( colorList[index],
            out colorTMP);
        colorImage.color = colorTMP;

        // Print the selected item's text to the console
        Debug.Log("Selected Item: " + color);
    }

    public void OnClickedQueryTag()
    {
        if (currentTagName.text == "")
        {
            infoText.text = "You must enter a tag name";
            infoText.color = Color.red;
            CleanFields();
            return;
        }

        isQueryClicked = true;
        
        string url = AppVariables.HttpServerUrl + "/tag/get-all" +
                     ListToQueryParameters.ListToQueryParams(
                         new[] { "name" }, new[] { currentTagName.text });
        StartCoroutine(GetTagByName(url));
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

            // Do things with _GetAllTagsResponseData 
            newTagName.text = _GetAllTagsResponseData[0].name;
            tagTypeDropdown.value = Array.IndexOf(tagTypeList, _GetAllTagsResponseData[0].tagType);
            colorDropdown.value = Array.IndexOf(colorList, _GetAllTagsResponseData[0].color);
            
            // Set the tagId, which will be used in updateTag request
            tagId = _GetAllTagsResponseData[0].id;

            infoText.text = "Such a tag exists";
            infoText.color = Color.green;
            
            Debug.Log("Success to update tag: " + response);

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
                    CleanFields();
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
                    CleanFields();
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