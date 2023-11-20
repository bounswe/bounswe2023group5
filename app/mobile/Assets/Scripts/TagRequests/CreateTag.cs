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

public class CreateTag : MonoBehaviour
{
    [SerializeField] private TMP_InputField tagName;
    [SerializeField] private TMP_Dropdown tagTypeDropdown;
    [SerializeField] private TMP_Dropdown colorDropdown;
    [SerializeField] private Button createTagButton;
    [SerializeField] private RawImage colorImage;
    [SerializeField] private TMP_Text infoText;
    [SerializeField] private Button exitButton;
    
    
    private CanvasManager canvasManager;
    private string tagType;
    private string color;

    private string[] colorList = new[]
    {
        "#8ECAE6", "#219EBC", "#023047", "#FFB703", "#FB8500",
        "#845A6D", "#A1E8AF", "#F5EE9E"
    };
    
    private void Awake()
    {
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;

        // Initial values of dropdown texts
        tagType = tagTypeDropdown.options[0].text;
        color = colorList[0];
        Color colorTMP;
        UnityEngine.ColorUtility.TryParseHtmlString( colorList[0],
            out colorTMP);
        colorImage.color = colorTMP;
        
        // Add listeners
        createTagButton.onClick.AddListener(OnClickedCreateTag);
        exitButton.onClick.AddListener(OnClickedExit);
        tagTypeDropdown.onValueChanged.AddListener(OnTagTypeValueChanged);
        colorDropdown.onValueChanged.AddListener(OnColorValueChanged);
    }

    public void OnClickedCreateTag()
    {
        string url = AppVariables.HttpServerUrl + "/tag/create";

        if (tagName.text == "")
        {
            infoText.text = "You must enter a tag name";
            return;
        }
        
        var createTagRequest = new CreateTagRequest();
        createTagRequest.name = tagName.text;
        createTagRequest.tagType = tagType;
        createTagRequest.color = color;
        
        Debug.Log($"name: {tagName.text}, tagType: {tagType}, "+ 
                  $"color: {color}");
        
        string bodyJsonString = JsonUtility.ToJson(createTagRequest);
        StartCoroutine(Post(url, bodyJsonString));
        
    }

    private void OnClickedExit()
    {
        canvasManager.HideCreateTagPage();
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
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        request.SetRequestHeader("Authorization", PersistenceManager.UserToken);
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        Debug.Log("line 93");
        if (request.responseCode == 200)
        {
            var _CreateTagResponseData = JsonConvert.DeserializeObject<TagResponse>(response);

            // Do things with _CreateTagResponseData 

            infoText.text = "Tag is successfully created";
            infoText.color = Color.green;
            
            Debug.Log("Success to create forum post: " + response);
        }
        else
        {
            Debug.Log("Error to create forum post: " + response);
            
            TagInfo _CreateTagResponseData;

            try
            {
                _CreateTagResponseData = JsonConvert.DeserializeObject<TagInfo>(response);
                infoText.text = _CreateTagResponseData.message ;
            }
            catch (JsonReaderException e)
            {
                Console.WriteLine(e);

                infoText.text = response;
            }

            
            infoText.color = Color.red;
                
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
}