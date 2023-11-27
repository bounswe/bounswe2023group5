using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using Color = System.Drawing.Color;

public class TagBox2 : MonoBehaviour
{
    [SerializeField] public TMP_Text tagName;
    [SerializeField] public Image image;

    public void Init(TagResponse tag)
    {
        tagName.text = tag.name;
        UnityEngine.Color tempColor;
        ColorUtility.TryParseHtmlString(tag.color, out tempColor);
        image.color = tempColor;
    }
}
