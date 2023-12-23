using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class Tag : MonoBehaviour
{
    [SerializeField] private TMP_Text tagName;
    [SerializeField] private Image image;
    
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void Init(TagResponse tagInfo)
    {
        Color color;

        if (ColorUtility.TryParseHtmlString(tagInfo.color, out color))
        {
            Debug.Log("Converted Color: " + color);
            this.image.color = color;
        }
        else
        {
            Debug.Log("Error converting color");
        }

        tagName.text = tagInfo.name;
    }
}
