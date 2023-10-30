using UnityEngine;
using UnityEngine.UI;
using System.Collections.Generic;
using TMPro;

public class GameDisplayManager : MonoBehaviour
{
    public Button searchButton;
    public Button filterButton;
    public TMP_InputField searchTextfield;
    public GameObject filterPopup;

    // Dropdown references
    public TMP_Dropdown playerTypeDropdown;
    public TMP_Dropdown genreDropdown;
    public TMP_Dropdown productionDropdown;
    public TMP_Dropdown durationDropdown;
    public TMP_Dropdown platformsDropdown;
    public TMP_Dropdown artStyleDropdown;
    public TMP_Dropdown developerDropdown;
    public TMP_Dropdown monetizationDropdown;

    private List<Tag> allTags;
    public Transform gamePageParent;

    private void Start()
    {
        searchButton.onClick.AddListener(OnSearchButtonClicked);
        filterButton.onClick.AddListener(OnFilterButtonClicked);
        
        filterPopup.SetActive(false);
    
        StartCoroutine(GameAPIHelper.GetAllTags(PopulateDropdowns));
        
        StartCoroutine(GameAPIHelper.GetAllGames(RefreshGameDisplay));
    }

    private void OnSearchButtonClicked()
    {
        string searchTerm = searchTextfield.text;

        List<Tag> selectedTags = GetSelectedTagsFromDropdowns();

        StartCoroutine(GameAPIHelper.GetAllGames(RefreshGameDisplay, selectedTags, searchTerm));
    }
    private void OnFilterButtonClicked()
    {
        filterPopup.SetActive(!filterPopup.activeSelf);
    }

    private void PopulateDropdowns()
    {
        allTags = GameDataHelper.GameData.tags;

        PopulateDropdownWithTagType(playerTypeDropdown, TagType.PlayerType);
        PopulateDropdownWithTagType(genreDropdown, TagType.Genre);
        PopulateDropdownWithTagType(productionDropdown, TagType.Production);
        PopulateDropdownWithTagType(durationDropdown, TagType.Duration);
        PopulateDropdownWithTagType(platformsDropdown, TagType.Platform);
        PopulateDropdownWithTagType(artStyleDropdown, TagType.ArtStyle);
        PopulateDropdownWithTagType(developerDropdown, TagType.Developer);
        PopulateDropdownWithTagType(monetizationDropdown, TagType.Monetization);
    }

    private void PopulateDropdownWithTagType(TMP_Dropdown dropdown, TagType type)
    {
        var tagsOfType = allTags.FindAll(tag => tag.type == type);
        dropdown.ClearOptions();
        List<string> tagNames = new List<string>();
        foreach (var tag in tagsOfType)
        {
            tagNames.Add(tag.name);
        }
        dropdown.AddOptions(tagNames);
    }

    private void RefreshGameDisplay()
    {
        var games = GameDataHelper.GameData.games;

        foreach (Transform child in gamePageParent)
        {
            Destroy(child.gameObject);
        }

        foreach (var gameInfo in games)
        {
            GamePage newGamePage = Instantiate(Resources.Load<GamePage>("Prefabs/GamePage"), gamePageParent);
            newGamePage.Init(gameInfo);
        }
    }

    
    private List<Tag> GetSelectedTagsFromDropdowns()
    {
        List<Tag> selectedTags = new List<Tag>();

        selectedTags.Add(GetTagFromDropdown(playerTypeDropdown));
        selectedTags.Add(GetTagFromDropdown(genreDropdown));
        selectedTags.Add(GetTagFromDropdown(productionDropdown));
        selectedTags.Add(GetTagFromDropdown(durationDropdown));
        selectedTags.Add(GetTagFromDropdown(platformsDropdown));
        selectedTags.Add(GetTagFromDropdown(artStyleDropdown));
        selectedTags.Add(GetTagFromDropdown(developerDropdown));

        return selectedTags;
    }

    private Tag GetTagFromDropdown(TMP_Dropdown dropdown)
    {
        string selectedTagName = dropdown.options[dropdown.value].text;
        return GameDataHelper.GameData.tags.Find(tag => tag.name == selectedTagName);
    }
}
