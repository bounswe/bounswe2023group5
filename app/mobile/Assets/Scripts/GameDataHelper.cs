using System;
using Newtonsoft.Json;
using UnityEngine;

public static class GameDataHelper
{
    private static GameData _gameData;

    public static GameData GameData
    {
        get
        {
            if (_gameData == null)
            {
                ReadGameDataFromResources();
            }
            return _gameData;
        }
        set
        {
            _gameData = value;
        }
    }

    private static void ReadGameDataFromResources()
    {
        string dataJson = Resources.Load<TextAsset>("Games").text;
        _gameData = JsonConvert.DeserializeObject<GameData>(dataJson);
    }
}