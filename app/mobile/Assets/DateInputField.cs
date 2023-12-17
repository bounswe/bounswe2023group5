using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using TMPro;
using UnityEngine;

public class DateInputField : MonoBehaviour
{
    public TMP_InputField input;
    bool allSlashAdded;
    
    int prevLength = 0;
 
    private void Awake()
    {
        input.onValueChanged.AddListener(delegate { OnValueChanged(); });
    }
    
    
 
    public void OnValueChanged() {
 
        allSlashAdded = input.text.Length >= 6;
        if (prevLength < input.text.Length)
        {
            if (!allSlashAdded && (input.text.Length == 2 || input.text.Length == 5))
            {
            
 
                input.text += "/";

                input.stringPosition = input.text.Length;
            }
        }
        else
        {
            if (input.text.Length == 2 || input.text.Length == 5)
            {
                input.text = input.text.Remove(input.text.Length - 1);
            }
        }
        prevLength = input.text.Length;
        
    }
}
