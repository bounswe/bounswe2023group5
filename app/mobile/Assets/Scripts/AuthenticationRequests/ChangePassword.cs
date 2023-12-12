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
using DG.Tweening;
using UnityEngine.Networking;

public class ChangePassword : MonoBehaviour
{
	public TMP_InputField passwordInputField;
	public TMP_InputField newPasswordInputField;
	public TMP_Text errorField;
	private string url = AppVariables.HttpServerUrl + "/auth/change-password";


	// After the change password message is sent, the user should be navigated to 
	// another page
	private CanvasManager canvasManager;


	private void Awake()
	{
		// Line below will be DELETED after log-in is implemented
		// PersistenceManager.UserToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJha2lsQG1haWwuY29tIiwiZXhwIjoxNjk5NTU1NzUzLCJpYXQiOjE2OTg2OTE3NTN9.Opz6PHi12gRZB0CnFUqOe0e1HIgs40gJqG_C_xhPEOE";
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

	    // Check new password field
	    if (newPasswordInputField.text.Length < 6)
	    {
		    string message = "New password must be at least 6 characters";
		    Debug.Log(message);
		    errorField.text = message;
		    errorField.color = Color.red;
			return;
	    }
	    
	    // Check the token exists
	    if (PersistenceManager.UserToken == "")
	    {
		    string message = "No user is logged in";
		    Debug.Log(message);
		    errorField.text = message;
		    errorField.color = Color.red;
		    return;
	    }
	    Debug.Log("token is "+ PersistenceManager.UserToken);

	    // "Password change request" object
	    PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
	    passwordChangeRequest.SetFields(passwordInputField.text, newPasswordInputField.text);
	    
	    // Make a request to the server
	    using (var client = new HttpClient())
	    {
		    Debug.Log(url + " " + PersistenceManager.UserToken);
		    // The backend "change password" endpoint
		    var endpoint = new Uri(url);
		    
		    // The body in json form
		    var newPostJson = JsonConvert.SerializeObject(passwordChangeRequest);
		    
		    // Configure the body
		    var payload = new StringContent(newPostJson, Encoding.UTF8, "application/json");

		    // Add authorization header
		    client.DefaultRequestHeaders.Add("Authorization", PersistenceManager.UserToken);
		    
		    // Make the request
		    var response = await client.PostAsync(endpoint, payload);

		    // Returns true if successful
		    string responseContent = await response.Content.ReadAsStringAsync();
		    
		    
		    // If returned success
		    if (responseContent == "true")
		    {
			    errorField.text = "Successfully registered";
			    errorField.color = Color.green;
            
			    
			    // 2 saniye sonra login sayfasına geç
			    DOVirtual.DelayedCall(2f, () =>
			    {
				    canvasManager.ShowLogInPage();
				    canvasManager.HideChangePasswordPage();
			    });
		    }
		    else
		    {
			    var message = "cannot change the password";
			    Debug.Log(message);
			    errorField.text = message;
			    errorField.color = Color.red;
		    }
		    
	    }
	    
	    
	    
	}
}
