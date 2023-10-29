using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.Net.Http;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class ChangePassword : MonoBehaviour
{
	public TMP_InputField emailInputField;
	public TMP_InputField passwordInputField;
	public TMP_InputField newPasswordInputField;
	public TMP_Text errorField;
	private string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/api/auth/change-password";


	private string token =
		"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYWxpc0BoYWxpcy5jb20iLCJleHAiOjE2OTk0NjUxMzYsImlhdCI6MTY5ODYwMTEzNn0.hMc9CDPEMQJA9StOCE4NLGfycnd5zEQ6FbGGXfxDHkw";	
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

    private async void SendPasswordChangeRequest()
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

	    // "Password change request" object
	    PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
	    passwordChangeRequest.SetFields(passwordInputField.text, newPasswordInputField.text);
	    
	    // Make a request to the server
	    using (var client = new HttpClient())
	    {
		    // The backend "change password" endpoint
		    var endpoint = new Uri(url);
		    
		    // The body in json form
		    var newPostJson = JsonConvert.SerializeObject(passwordChangeRequest);
		    
		    // Configure the body
		    var payload = new StringContent(newPostJson, Encoding.UTF8, "application/json");

		    // Add authorization header
		    client.DefaultRequestHeaders.Add("Authorization", token);
		    
		    // Make the request
		    var response = await client.PostAsync(endpoint, payload);

		    // Returns true if successful
		    string responseContent = await response.Content.ReadAsStringAsync();
		    
		    
		    // If returned success
		    if (responseContent == "true")
		    {
			    canvasManager.ShowHomePage();
			    canvasManager.HideChangePasswordPage();
		    }
		    else
		    {
			    var message = "cannot change the password";
			    Debug.Log(message);
			    errorField.text = message;
		    }
		    Debug.Log(responseContent == "true" ? "Password changed" : "Password not changed");

	    }
	    
	    
	    
	}
}
