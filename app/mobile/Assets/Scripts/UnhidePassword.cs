using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;
using UnityEngine.EventSystems;

public class UnhidePassword : MonoBehaviour, IPointerDownHandler, IPointerUpHandler
{
    public TMP_InputField passwordInputField;
    public TMP_Text passwordText;
    private string password;

    public void OnPointerDown(PointerEventData eventData)
    {
        password = passwordText.text;
        passwordInputField.inputType = TMP_InputField.InputType.Standard;
        passwordText.text = passwordInputField.text;
    }

    public void OnPointerUp(PointerEventData eventData)
    {
        passwordInputField.inputType = TMP_InputField.InputType.Password;
        passwordText.text = password;
    }
}
