using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEditor;
using Object = UnityEngine.Object;

public class CharacterDetails : MonoBehaviour
{
    private CanvasManager canvasManager;
    
    [SerializeField] private Image charImage;
    [SerializeField] private Button exitButton;

    [SerializeField] private TMP_Text name;
    [SerializeField] private TMP_Text description;
    [SerializeField] private TMP_Text Type1;
    [SerializeField] private TMP_Text Type2;
    [SerializeField] private TMP_Text Type3;
    [SerializeField] private TMP_Text Type4;
    [SerializeField] private TMP_Text Type5;
    [SerializeField] private TMP_Text Type6;
    [SerializeField] private TMP_Text Type7;
    [SerializeField] private TMP_Text Type8;
    [SerializeField] private TMP_Text Type9;
    [SerializeField] private TMP_Text Type10;
    [SerializeField] private TMP_Text Type11;
    [SerializeField] private TMP_Text Type12;
    [SerializeField] private TMP_Text Value1;
    [SerializeField] private TMP_Text Value2;
    [SerializeField] private TMP_Text Value3;
    [SerializeField] private TMP_Text Value4;
    [SerializeField] private TMP_Text Value5;
    [SerializeField] private TMP_Text Value6;
    [SerializeField] private TMP_Text Value7;
    [SerializeField] private TMP_Text Value8;
    [SerializeField] private TMP_Text Value9;
    [SerializeField] private TMP_Text Value10;
    [SerializeField] private TMP_Text Value11;
    [SerializeField] private TMP_Text Value12;

    private CharacterResponse charInfo;

    private string[] defaultFields = new[]
    {
        "type", "gender" , "race", "status", "occupation", "birthDate", "voiceActor",
        "height", "age"
    };

    private Object customFieldsObj;
    // private FieldInfo[] customFields;
    private int customIndex;
    private int customLength;

    private void Awake()
    {
        //Init();
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
        exitButton.onClick.AddListener(exit);
    }

    public void Init(CharacterResponse charInfoVal)
    {
        charInfo = charInfoVal;
        //customFieldsObj = charInfo.customFields;
        //customFields = charInfo.customFields.GetType().GetFields(BindingFlags.Instance | BindingFlags.Public);
        customIndex = 0;
        //customLength = customFields.Length;

        if (!charInfo.icon.Contains("webp"))
        {
            StartCoroutine(LoadImageFromURL(AppVariables.HttpImageUrl + charInfo.icon, charImage));
        }

        Debug.Log("charInfo is "+ customFieldsObj);

        name.text = charInfo.name;
        description.text = charInfo.description;
        SetAttributes();

    }

    private void SetAttributes()
    {
        SetAttribute(Type1, Value1, 0);
        SetAttribute(Type2, Value2, 1);
        SetAttribute(Type3, Value3, 2);
        SetAttribute(Type4, Value4, 3);
        SetAttribute(Type5, Value5, 4);
        SetAttribute(Type6, Value6, 5);
        SetAttribute(Type7, Value7, 6);
        SetAttribute(Type8, Value8, 7);
        SetAttribute(Type9, Value9, 8);
        // SetRemainingAttributes();
        
    }

    private void SetAttribute(TMP_Text typeObj, TMP_Text valueObj,int index)
    {
        /*
        if(String.IsNullOrEmpty((string) GetFieldValue(charInfo, defaultFields[index])))
        {
            if (customIndex < customLength)
            {
                typeObj.text = customFields[customIndex].Name;
                valueObj.text = (string)customFields[customIndex].GetValue(customFieldsObj);
                customIndex++;
            }
            else
            {
                typeObj.text = "";
                valueObj.text = "";
            }
        }
        else
        {
        */
            typeObj.text = defaultFields[index];
            valueObj.text = (string)GetFieldValue(charInfo, defaultFields[index]);
        //}
    }

    /*
    private void SetRemainingAttributes()
    {
        if (customIndex < customLength)
        {
            Type10.text = customFields[customIndex].Name;
            Value10.text = (string)customFields[customIndex].GetValue(customFieldsObj);
            customIndex++;
        }else
        {
            Type10.text = "";
            Value10.text = "";
        }
        if (customIndex < customLength)
        {
            Type11.text = customFields[customIndex].Name;
            Value11.text = (string)customFields[customIndex].GetValue(customFieldsObj);
            customIndex++;
        }else
        {
            Type11.text = "";
            Value11.text = "";
        }
        if (customIndex < customLength)
        {
            Type12.text = customFields[customIndex].Name;
            Value12.text = (string)customFields[customIndex].GetValue(customFieldsObj);
            customIndex++;
        }else
        {
            Type12.text = "";
            Value12.text = "";
        }
    }
    */
    
    static object GetFieldValue(object obj, string fieldName)
    {
        Type type = obj.GetType();
        FieldInfo field = type.GetField(fieldName, BindingFlags.Public | BindingFlags.Instance);

        if (field != null)
        {
            return field.GetValue(obj);
        }
        else
        {
            // Handle the case where the field with the given name doesn't exist
            Console.WriteLine($"Field '{fieldName}' not found.");
            return null;
        }
    }
    
    private IEnumerator LoadImageFromURL(string imageUrl, Image targetImage)
    {
        if (imageUrl.Contains("webp"))
        {
            yield return null;
        }
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(imageUrl);
        yield return request.SendWebRequest();

        if(request.result != UnityWebRequest.Result.Success)
        {
            // Debug.LogError("Failed to load image: " + request.error);
        }
        else
        {
            Texture2D texture = DownloadHandlerTexture.GetContent(request);
            targetImage.sprite = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }
    }

    private void exit()
    {
        canvasManager.HideCharacterDetailsPage();
    }
}
