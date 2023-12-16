package com.app.annotation.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private SelectorDto selector;
}
