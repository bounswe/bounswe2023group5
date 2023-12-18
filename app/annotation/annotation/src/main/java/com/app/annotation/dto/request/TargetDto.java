package com.app.annotation.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetDto {

    private String id;

    private String type;

    private String format;

    private String textDirection;

    @NotEmpty(message = "target source can not be empty.")
    private String source;

    @NotEmpty(message = "target selector can not be empty.")
    private List<SelectorDto> selector;
}
