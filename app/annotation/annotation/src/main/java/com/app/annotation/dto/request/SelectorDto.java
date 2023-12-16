package com.app.annotation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectorDto {

    @Pattern(regexp = "TextQuoteSelector", message = "Currently only Text Quote Selector is supported.")
    private String type;

    @NotEmpty(message = "exact value of Text Quote Selector can not be empty.")
    private String exact;

    private String prefix;

    private String suffix;
}
