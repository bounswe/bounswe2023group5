package com.app.annotation.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectorDto {

    @Pattern(regexp = "^(TextQuoteSelector|TextPositionSelector)$",
            message = "Currently only Text Quote Selector and Text Position Selector are supported.")
    private String type;

    private String exact;

    private String prefix;

    private String suffix;

    @PositiveOrZero
    private Integer start;

    @Positive
    private Integer end;

    private String value;

    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].\\S*$", message = "Invalid URL")
    private String conformsTo;
}
