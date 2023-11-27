using System.Collections;
using System.Collections.Generic;
using UnityEngine;



public class openComment : MonoBehaviour
{

    public ForumPostComments forumPostComment; 
    // Start is called before the first frame update
    void Start()
    {
        
    }

    public void openLocalPostComment(string id, GetPostListResponse postInfoVal)
    {
        forumPostComment.gameObject.SetActive(true);
        forumPostComment.Init(id, postInfoVal);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
