using System.Collections.Generic;

public static class DictionaryToQueryParameters
{
    public static string DictionaryToQuery(Dictionary<string,string> queryParams)
    {
        string queryParamsString = "";

        bool isQueryParamAdded = false;
        
        foreach (KeyValuePair<string, string> entry in queryParams)
        {
            if (!isQueryParamAdded) // First query param added
            {
                queryParamsString = queryParamsString + "?";
                isQueryParamAdded = true;
            }
            else
            {
                queryParamsString =  queryParamsString + "&";
            }
            if (entry.Value == "" && entry.Value == null)
            {
                continue;
            }
            if (entry.Value == "true")
            {
                queryParamsString += $"{entry.Key}={true}";
            }
            else if (entry.Value == "false")
            {
                queryParamsString += $"{entry.Key}={false}";
            }
            else
            {
                queryParamsString += $"{entry.Key}={entry.Value}";
            }
        }
        return queryParamsString;
    }
    
    // Return the value of the param. If param does not exist in pars,
    // return empty string ""
    public static string GetValueOfParam(Dictionary<string,string> queryParams, string param)
    {
        if (queryParams.Count == 0) return "";
        
        foreach (KeyValuePair<string, string> entry in queryParams)
        {
            if (entry.Key == param)
            {
                return entry.Value;
            }
        }
        return "";
    }
    
    
}