// GameData.cs

using System;
using System.Collections.Generic;

public class GameData
{
    public GameInfo[] games;
    public List<Tag> tags; // To store the tags
}


public class GameInfo
{
    public string gameID;
    public string gameName;
    public string gameIcon = "default_icon.png";
    public string gameDescription;
    public List<Tag> playerType;
    public List<Tag> genre;
    public Tag production;
    public Tag duration;
    public List<Tag> platforms;
    public List<Tag> artStyle;
    public Tag developer;
    public DateTime releaseDate;
    public string minSysReq;
    public Tag monetization;
    public List<Tag> otherTags;
}

public class Tag
{
    public string tag_id;
    public string name;
    public TagType type;
    public string color;
}

public enum TagType { PlayerType, Genre, Production, Duration, Platform, ArtStyle, Developer, Monetization }