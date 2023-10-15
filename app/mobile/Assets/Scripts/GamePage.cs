using UnityEngine;
using TMPro;
using UnityEngine.UI;

public class GamePage : MonoBehaviour
{
    [SerializeField] private Image gameImage;
    [SerializeField] private TMP_Text gameName;
    [SerializeField] private TMP_Text gameDescription;
    
    public void Init(GameInfo gameInfo)
    {
        gameImage.sprite = Resources.Load<Sprite>("images/" + gameInfo.name);
        gameName.text = gameInfo.name;
        gameDescription.text = gameInfo.info;
    }
}
