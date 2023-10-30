using System;
using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

public static class GameAPIHelper
{
    private const string BASE_URL = "Your_API_Endpoint_Here";

    public static IEnumerator GetAllTags(Action onComplete = null)
    {
        string url = BASE_URL + "/api/get-all-tags";
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.isNetworkError || request.isHttpError)
        {
            //Debug.LogError(request.error);
        }
        else
        {
            List<Tag> fetchedTags = JsonConvert.DeserializeObject<List<Tag>>(request.downloadHandler.text);
            GameDataHelper.GameData.tags = fetchedTags;
            onComplete?.Invoke();
        }
    }
    
    public static IEnumerator GetAllGames(Action callback = null, List<Tag> filterTags = null, string searchText = null)
    {
        string url = $"{BASE_URL}/api/get-game-list";

        WWWForm form = new WWWForm();

        if (filterTags != null && filterTags.Count > 0)
        {
            form.AddField("tags", JsonConvert.SerializeObject(filterTags));
        }

        if (!string.IsNullOrEmpty(searchText))
        {
            form.AddField("searchText", searchText);
        }

        UnityWebRequest request = UnityWebRequest.Post(url, form);

        yield return request.SendWebRequest();

        if (request.result != UnityWebRequest.Result.Success)
        {
            // Debug.LogError(request.error);
        }
        else
        {
            GameData fetchedGameData = JsonConvert.DeserializeObject<GameData>(request.downloadHandler.text);
            GameDataHelper.GameData.games = fetchedGameData.games;
            callback?.Invoke();
        }
    }
}