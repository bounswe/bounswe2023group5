using System;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class DeleteAccount : MonoBehaviour
{
    public TMP_InputField passwordInputField;
    private CanvasManager canvasManager;
    [SerializeField] private Button deleteButton;
    [SerializeField] private Toggle checkBox;

    
    private void Awake()
    {
        deleteButton.onClick.AddListener(OnClickedDelete);
        
        canvasManager = FindObjectOfType(typeof(CanvasManager)) as CanvasManager;
    }

    private void OnClickedDelete()
    {
        bool isConfirmed = checkBox.isOn;
    }


}