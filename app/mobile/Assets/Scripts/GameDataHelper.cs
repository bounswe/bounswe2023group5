using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

public static class GameDataHelper
{
    private static GameData _gameData;
    public static GameData GameData
    {
        get
        {
            if (_gameData != null) return _gameData;
            ReadGameData();
            return _gameData;
        }
    }
    
    private static void ReadGameData()
    {
        string userJson = Resources.Load<TextAsset>("Games").text;
        _gameData = JsonConvert.DeserializeObject<GameData>(userJson);
    }
}