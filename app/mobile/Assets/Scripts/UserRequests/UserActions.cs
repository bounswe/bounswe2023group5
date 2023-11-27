using System.Collections;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class UserActions : MonoBehaviour
{

    public void ChangeUserRole(UserChangeRoleRequest request)
    {
        string url = AppVariables.HttpServerUrl + "/user/change-role";
        string bodyJsonString = JsonConvert.SerializeObject(request);
        StartCoroutine(Post(url, bodyJsonString));
    }

    public void GetAllUsers(UserGetAllRequest request)
    {
        string url = AppVariables.HttpServerUrl + "/user/get-all" + 
                     ListToQueryParameters.ListToQueryParams(new[] {"id", "username", "isDeleted"},
                                                              new[] {request.id, request.username, request.isDeleted.ToString()});
        StartCoroutine(Get(url));
    }

    public void DeleteUser(UserDeleteRequest request)
    {
        string url = AppVariables.HttpServerUrl + "/user/delete?id=" + request.id;
        StartCoroutine(Delete(url));
    }

    IEnumerator Get(string url)
    {
        var request = new UnityWebRequest(url, "GET");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
    }

    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        // Handle the response
    }

    IEnumerator Delete(string url)
    {
        var request = new UnityWebRequest(url, "DELETE");
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
    }

}
