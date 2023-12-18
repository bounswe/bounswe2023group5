using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class UploadImage : MonoBehaviour
{
    [SerializeField] private Image image;
    [SerializeField] private string folder;
    private Dictionary<string,string> queryParams = new Dictionary<string, string>();

    private void Start()
    {
        // queryParams.Add("folder", "");
        // Init();
        // StartCoroutine(UploadSprite(image, folder));
        string url = "http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/achievement-icons/commentator.jpg";
        StartCoroutine(DownloadImage(url));
    }

    public void Init()
    {
        string url = AppVariables.HttpServerUrl + $"/image/upload?folder={folder}";
        var imageUploadRequest = new ImageUploadRequest();
        Texture2D textureNew = DeCompress(image.sprite.texture);
        byte[] imageBytes = textureNew.EncodeToPNG();
        string imageString = Convert.ToBase64String(imageBytes);
        imageUploadRequest.image = imageString;
        string bodyJsonString = JsonUtility.ToJson(imageUploadRequest);
        StartCoroutine(Post(url, bodyJsonString));
    }
    IEnumerator Post(string url, string bodyJsonString)
    {
        var request = new UnityWebRequest(url, "POST");
        byte[] bodyRaw = Encoding.UTF8.GetBytes(bodyJsonString);
        request.uploadHandler = new UploadHandlerRaw(bodyRaw);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        string response = "";
        if (request.responseCode == 200)
        {
            response = request.downloadHandler.text;
            Debug.Log("Success to upload image: " + response);
        }
        else
        {
            Debug.Log("Error to upload image: " + response);
        }
        request.downloadHandler.Dispose();
        request.uploadHandler.Dispose();
    }
    
    IEnumerator UploadSprite(Image image, string folder)
    {
        Texture2D textureNew = DeCompress(image.sprite.texture);
        byte[] imageBytes = textureNew.EncodeToPNG();
        string imageString = Convert.ToBase64String(imageBytes);
        
        WWWForm form = new WWWForm();
        form.AddField("image", imageString);
        
        UnityWebRequest request = UnityWebRequest.Post($"{AppVariables.HttpServerUrl}/image/upload?folder={folder}", form);
        request.chunkedTransfer = false;
        request.downloadHandler = new DownloadHandlerBuffer();
        // request.SetRequestHeader("Content-Type", "application/json");

        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            Debug.Log("Image upload successful!");
            Debug.Log(request.downloadHandler.text); // Response data
        }
        else
        {
            Debug.LogError("Image upload failed: " + request.error);
        }
        request.downloadHandler.Dispose();
    }
    
    Texture2D SpriteToTexture2D(Sprite sprite)
    {
        // Sprite'ın texture'ını elde etme
        Texture2D texture = sprite.texture;

        // Yeni bir Texture2D oluşturma ve Sprite'ın texture'ını kopyalama
        Texture2D newTexture = new Texture2D((int)sprite.rect.width, (int)sprite.rect.height);
        Color[] pixels = texture.GetPixels((int)sprite.textureRect.x, (int)sprite.textureRect.y, (int)sprite.textureRect.width, (int)sprite.textureRect.height);
        newTexture.SetPixels(pixels);
        newTexture.Apply();

        return newTexture;
    }
    
    public static Texture2D DeCompress(Texture2D source)
    {
        RenderTexture renderTex = RenderTexture.GetTemporary(
            source.width,
            source.height,
            0,
            RenderTextureFormat.Default,
            RenderTextureReadWrite.Linear);

        Graphics.Blit(source, renderTex);
        RenderTexture previous = RenderTexture.active;
        RenderTexture.active = renderTex;
        Texture2D readableText = new Texture2D(source.width, source.height);
        readableText.ReadPixels(new Rect(0, 0, renderTex.width, renderTex.height), 0, 0);
        readableText.Apply();
        RenderTexture.active = previous;
        RenderTexture.ReleaseTemporary(renderTex);
        return readableText;
    }
    
    
    public string apiUrl = AppVariables.HttpServerUrl; // API URL'sini buraya giriniz.
    public string folderName = "achievement-icons"; // Yüklenecek klasör adını buraya giriniz.

    // Bu fonksiyon, belirtilen dosya yoluyla bir resim yüklemek için kullanılır.
    public IEnumerator UploadImage2(string imagePath)
    {
        byte[] imageData = System.IO.File.ReadAllBytes(imagePath);
        WWWForm form = new WWWForm();
        form.AddBinaryData("image", imageData, "gamer.png", "image/png");

        UnityWebRequest request = UnityWebRequest.Post(apiUrl + "/image/upload?folder=" + folderName, form);
        yield return request.SendWebRequest();

        if (request.result != UnityWebRequest.Result.Success)
        {
            Debug.LogError("Image upload failed: " + request.error);
        }
        else
        {
            Debug.Log("Image uploaded successfully. Response: " + request.downloadHandler.text);
        }
    }
    
    public IEnumerator DownloadImage2(string folder, string filename)
    {
        string url = $"http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/{folder}/{filename}";
        using (UnityWebRequest request = UnityWebRequest.Get(url))
        {
            yield return request.SendWebRequest();

            if (request.result != UnityWebRequest.Result.Success)
            {
                Debug.LogError("Error: " + request.error);
            }
            else
            {
                // İsteğin başarılı olduğu durumda, alınan veriyi işleyebilirsiniz.
                // Örneğin, indirilen resmi bir Texture olarak kullanabilirsiniz:
                Texture2D texture = DownloadHandlerTexture.GetContent(request);
                // Texture ile ne yapacağınıza bağlı olarak burada işlemler yapabilirsiniz.
            }
        }
    }
    
    IEnumerator DownloadImage(string MediaUrl)
    {   
        UnityWebRequest request = UnityWebRequestTexture.GetTexture(MediaUrl);
        yield return request.SendWebRequest();
        if (request.isNetworkError || request.isHttpError)
        {
            Debug.Log("-----------");
            Debug.Log(request.error);
        }
        else
        {
            Texture2D texture2;
            texture2 = ((DownloadHandlerTexture) request.downloadHandler).texture;
            Sprite sprite = Sprite.Create(texture2, new Rect(0, 0, texture2.width, texture2.height), new Vector2(0, 0));
        }
    } 

}
