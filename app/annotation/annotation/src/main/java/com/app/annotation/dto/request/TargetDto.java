package com.app.annotation.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
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
    @JsonIgnore
    private List<SelectorDto> selector;

    @JsonSetter("selector")
    public void setSelector(Object selector) {
        if (selector instanceof List) {
            List<LinkedHashMap<String, Object>> hashList;
            hashList = (List<LinkedHashMap<String, Object>>) selector;
            this.selector = mapToSelectorDtoList(hashList);
        } else {
            LinkedHashMap<String, Object> hash = (LinkedHashMap<String, Object>) selector;
            this.selector = Collections.singletonList(convertToSelectorDto(hash));
        }
    }

    public static List<SelectorDto> mapToSelectorDtoList(List<LinkedHashMap<String, Object>> inputList) {
        List<SelectorDto> resultList = new ArrayList<>();

        if (inputList != null) {
            for (LinkedHashMap<String, Object> map : inputList) {
                SelectorDto selectorDto = convertToSelectorDto(map);
                resultList.add(selectorDto);
            }
        }

        return resultList;
    }

    private static SelectorDto convertToSelectorDto(LinkedHashMap<String, Object> map) {
        SelectorDto selectorDto = new SelectorDto();
        selectorDto.setType((String) map.get("type"));
        selectorDto.setExact((String) map.get("exact"));
        selectorDto.setPrefix((String) map.get("prefix"));
        selectorDto.setSuffix((String) map.get("suffix"));
        selectorDto.setStart((Integer) map.get("start"));
        selectorDto.setEnd((Integer) map.get("end"));
        selectorDto.setValue((String) map.get("value"));
        selectorDto.setConformsTo((String) map.get("conformsTo"));
        // Add other fields as needed

        return selectorDto;
    }
}
