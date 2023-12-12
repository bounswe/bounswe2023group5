using System;
using System.Reflection;
using UnityEngine;
using Object = System.Object;

public static class ListToQueryParameters
{
    // When list of the parameters and list of their values are given,
    // a query string is returned.
    // This string should be added after the url
    public static string ListToQueryParams(string[] pars, string[] values)
    {
        string queryParams = "";

        bool isQueryParamAdded = false;
        
        for(int i =0; i < pars.Length ; i++)
        {
            if (!isQueryParamAdded) // First query param added
            {
                queryParams = queryParams + "?";
                isQueryParamAdded = true;
            }
            else
            {
                queryParams =  queryParams+ "&";
            }

            queryParams += $"{pars[i]}={values[i]}";
        }


        return queryParams;
    }

    // Return the value of the param. If param does not exist in pars,
    // return empty string ""
    public static string GetValueOfParam(string[] pars, string[] values, string param)
    {
        if (pars.Length == 0) return "";
        
        int i ;

        for (i = 0; i < pars.Length; i++)
        {
            if (pars[i] == param)
            {
                return values[i];
            }
        }

        return "";
    }
}