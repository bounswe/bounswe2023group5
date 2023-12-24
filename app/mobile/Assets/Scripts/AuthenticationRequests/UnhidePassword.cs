using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;
using UnityEngine.EventSystems;

public class UnhidePassword : MonoBehaviour, IPointerDownHandler
{
    public TMP_InputField passwordInputField;
    public TMP_Text passwordText;
    public Sprite unhidePasswordSprite;
    public Sprite hidePasswordSprite;
    public Image image;
    private string password;
    private bool isPasswordHidden = true;
    
    private void Awake()
    {
        hidePasswordSprite = Resources.Load<Sprite>("images/hidePassword");
        unhidePasswordSprite = Resources.Load<Sprite>("images/unhidePassword");
        image = GetComponent<Image>();
    }

    public void OnPointerDown(PointerEventData eventData)
    {
        if (isPasswordHidden)
        {
            password = passwordText.text;
            passwordInputField.inputType = TMP_InputField.InputType.Standard;
            passwordText.text = passwordInputField.text;
            image.sprite = unhidePasswordSprite;
        }
        else
        {
            passwordInputField.inputType = TMP_InputField.InputType.Password;
            passwordText.text = password;
            image.sprite = hidePasswordSprite;
        }
        isPasswordHidden = !isPasswordHidden;
        
    }

    // public void OnPointerUp(PointerEventData eventData)
    // {
    //     passwordInputField.inputType = TMP_InputField.InputType.Password;
    //     passwordText.text = password;
    // }
}
