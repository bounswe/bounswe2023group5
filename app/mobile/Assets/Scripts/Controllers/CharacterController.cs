using UnityEngine;

public class CharacterController
{
    
}

// CharacterUpdateRequest == CharacterRequest
// Query parameters:
// string id
// Header parameters:
// string Authorization
// Response is CharacterResponse

// CharacterCreateRequest == CharacterRequest
// Header parameters:
// string Authorization


public class CharacterRequest
{
    public string name;
    public string icon;
    public string description;
    public string[] games;
    public string type;
    public string gender;
    public string race;
    public string status;
    public string occupation;
    public string birthDate;
    public string voiceActor;
    public string height;
    public string age;
    public Object customFields;
}

// CharacterGetGameCharactersRequest
// Query parameters:
// string gameId
// Response is CharacterResponse[]

// CharacterDeleteRequest
// Query parameters:
// string id
// Header parameters:
// string Authorization
// Response is CharacterResponse


public class CharacterResponse
{
    public string id;
    public string createdAt;
    public bool isDeleted;
    public string name;
    public string icon;
    public string description;
    public string[] games;
    public string type;
    public string gender;
    public string race;
    public string status;
    public string occupation;
    public string birthDate;
    public string voiceActor;
    public string height;
    public string age;
    public Object customFields;
}


/*
public class CustomFields
{
    public string additionalProp1;
    public string additionalProp2;
    public string additionalProp3;
}
*/