using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using UnityEngine.Networking;

public class CharacterPage : MonoBehaviour
{
    private List<Character> charObjects = new List<Character>();
    [SerializeField] private Transform charObjParent;
    [SerializeField] private ScrollRect charScroll;
    private string gameId;

    public void Awake()
    {
        Init("841cbf45-90cc-47b7-a763-fa3a18218bf9");
    }

    public void Init(string gameIdVal)
    {
        gameId = gameIdVal;
        ShowCharacters();
    }

    private void ShowCharacters()
    {
        string url = $"{AppVariables.HttpServerUrl}/character/get-game-characters" +
                     ListToQueryParameters.ListToQueryParams(new[] { "gameId" }, new[] { gameId });

        StartCoroutine(GetCharacters(url));
    }
    
    IEnumerator GetCharacters(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        response = request.downloadHandler.text;
        if (request.responseCode == 200)
        {
            var charactersResponseData = JsonConvert.DeserializeObject<CharacterResponse[]>(response);

            // Do things with _GetAllTagsResponseData 
            AddChacters(charactersResponseData);
            Debug.Log("Success to list characters: " + response);
        }
        else
        {
            Debug.Log("Error to list characters: " + response);
        }
        request.downloadHandler.Dispose();
    }
    
    private void AddChacters(CharacterResponse[] characters)
    {
        foreach (var charObj in charObjects)
        {
            Destroy(charObj.gameObject);
        }
        charObjects.Clear();

        foreach (var character in characters)
        {
            Character tagObj = Instantiate(Resources.Load<Character>("Prefabs/Character"), charObjParent);
            charObjects.Add(tagObj);
            tagObj.Init(character);
        }
        Canvas.ForceUpdateCanvases();
        charScroll.horizontalNormalizedPosition = 1;
        Debug.Log("Success to list tags");
    }
}
