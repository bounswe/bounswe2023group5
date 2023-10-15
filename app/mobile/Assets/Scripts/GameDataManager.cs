using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameDataManager : MonoBehaviour
{
    public Transform gamePageParent;
    void Start()
    {
        foreach (var gameInfo in GameDataHelper.GameData.games)
        {
            GamePage newGamePage = Instantiate(Resources.Load<GamePage>("Prefabs/GamePage"), gamePageParent);
            newGamePage.Init(gameInfo);
        }
    }
}
