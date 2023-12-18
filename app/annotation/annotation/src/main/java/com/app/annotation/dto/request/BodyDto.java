package com.app.annotation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodyDto {

    @Pattern(regexp = "TextualBody", message = "Currently only Embedded Textual Bodies are supported.")
    private String type;

    private String id;

    private String purpose;

    @NotEmpty(message = "body value can not be empty.")
    private String value;

    private String format;

    private String language;
}
