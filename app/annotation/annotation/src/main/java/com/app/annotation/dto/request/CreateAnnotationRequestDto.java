package com.app.annotation.dto.request;

import com.app.annotation.model.Creator;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnnotationRequestDto {

    @NotEmpty(message = "annotation target can not be empty.")
    private TargetDto target;

    @NotEmpty(message = "annotation body can not be empty.")
    private BodyDto body;

    @NotEmpty(message = "annotation id can not be empty.")
    private String id;

    @NotEmpty(message = "annotation type can not be empty.")
    private String type;

    private String motivation;

    private Date created;

    private Creator creator;
}
