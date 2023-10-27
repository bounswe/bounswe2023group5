package com.app.gamereview.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGameListRequestDto {
    
    private Boolean findDeleted;
    
}
