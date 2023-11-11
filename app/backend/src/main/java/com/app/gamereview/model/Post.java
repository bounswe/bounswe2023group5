package com.app.gamereview.model;

import java.time.LocalDateTime;
import java.util.List;

import com.app.gamereview.enums.VoteChoice;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.gamereview.model.common.BaseModel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "Post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseModel {

  private String title;

  private String postContent;

  private String postImage;

  private String poster;

  @NotNull
  private String forum;

  // TODO avatar

  private LocalDateTime lastEditedAt;

  private List<String> tags;

  private Boolean inappropriate;

  private Boolean locked;

  private int overallVote; // overallVote = # of upvote - # of downvote

  private int voteCount;  // voteCount = # of upvote + # of downvote

  // TODO reports
  // TODO comments
  // TODO annotations
  // TODO achievements

  public void addVote(VoteChoice choice){
    voteCount += 1;
    if(choice.name().equals("UPVOTE")){
      overallVote += 1;
    }
    else if(choice.name().equals("DOWNVOTE")){
      overallVote -= 1;
    }
  }

  public void deleteVote(VoteChoice choice){
    voteCount -= 1;
    if(choice.name().equals("UPVOTE")){
      overallVote -= 1;
    }
    else if(choice.name().equals("DOWNVOTE")){
      overallVote += 1;
    }
  }


}
