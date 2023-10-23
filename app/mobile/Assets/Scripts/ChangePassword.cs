using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class ChangePassword : MonoBehaviour
{
	public TMP_InputField emailInputField;
	public TMP_InputField passwordInputField;
	public TMP_InputField newPasswordInputField;
	public TMP_Text errorField;
	
	// After the change password message is sent, the user should be navigated to 
	// another page
	private CanvasManager canvasManager;

	private void Awake()
	{
		GetComponent<Button>().onClick.AddListener(SendPasswordChangeRequest);
		canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
	}

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    private void SendPasswordChangeRequest()
    {
	    // check the email field
	    if (emailInputField.text.Contains("@") == false || emailInputField.text.Contains(".") == false)
	    {
		    Debug.Log("Email is not valid");
			errorField.text = "Email is not valid";
		    return;
	    }

	    // Check new password field
	    if (newPasswordInputField.text.Length < 6)
	    {
		    Debug.Log("New password must be at least 6 characters");
		    errorField.text = "New password must be at least 6 characters";
			return;
	    }

	    PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
	    passwordChangeRequest.email = emailInputField.text;
	    passwordChangeRequest.password = passwordInputField.text;
	    passwordChangeRequest.newPassword = newPasswordInputField.text;
	    
	    // Here I will normally put a function to make a password change
	    // request to the back-end. And depending on the response, print
	    // a message to the page

	    var (isPasswordChanged, message) = ChangePasswordHelper.ChangeUserPassword(passwordChangeRequest);
	    
	    if (isPasswordChanged)
	    {
		    canvasManager.ShowHomePage();
		    canvasManager.HideChangePasswordPage();
	    }else
		{
			Debug.Log(message);
			errorField.text = message;
		}
		Debug.Log(isPasswordChanged ? "Password changed" : "Password not changed");
	}
}
