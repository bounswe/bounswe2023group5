using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.PlayerLoop;
using UnityEngine.UI;

public class MultySelectDopdown : MonoBehaviour
{
    
    [Header("Settings")]
    public string dropdownName;
    
    
    [Header("References")]
    [SerializeField] private GameObject dropdown;
    [SerializeField] private GameObject dropdownContent;
    [SerializeField] private Toggle dropdownItemPrefab;
    [SerializeField] private Button button;
    
    private List<(string,string)> dropDownItems = new List<(string, string)>();
    private List<(Toggle, string)> toggleItems = new List<(Toggle, string)>();
    private List<string> selectedItemsID = new List<string>();

    private void Awake()
    {
        button.onClick.AddListener(OnClickedButton);
    }

    private void OnClickedButton()
    {
        dropdown.SetActive(!dropdown.activeSelf);
    }
    
    private void ClearDropdown()
    {
        foreach (Transform child in dropdownContent.transform)
        {
            Destroy(child.gameObject);
        }
    }

    // private List<(string,string)> list = new List<string>("1,2,3,4,5,6,7,8,9,10".Split(',')).ConvertAll(x => (x, x));
    
    public void InitDropdown(List<(string,string)> items)
    {
        ClearDropdown();
        dropDownItems = items;
        foreach (var (item,itemID) in items)
        {
            var dropdownItem = Instantiate(dropdownItemPrefab, dropdownContent.transform);
            dropdownItem.gameObject.SetActive(true);
            dropdownItem.GetComponentInChildren<TextMeshProUGUI>().text = item;
            dropdownItem.isOn = false;
            toggleItems.Add((dropdownItem,itemID));
            dropdownItem.onValueChanged.AddListener(delegate { SelectedItem(dropdownItem.GetComponentInChildren<Toggle>()); });
        }
    }
    
    public void SelectedItem(Toggle toggle)
    {
        selectedItemsID.Clear();
        foreach (var (item ,itemID) in toggleItems)
        {
            if (item.isOn)
            {
                selectedItemsID.Add(itemID);
            }
        }
        Debug.Log(string.Join(",",selectedItemsID));
    }
    
    public List<(string,string)> GetSelectedItems()
    {
        List<(string,string)> newSelectedItems = new List<(string, string)>();
        foreach (var item in selectedItemsID)
        {
            newSelectedItems.Add((dropdownName,item));
        }
        return newSelectedItems;
    }

        
}
