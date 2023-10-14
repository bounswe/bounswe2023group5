package com.app.gamereview.controller;

import com.app.gamereview.model.SampleModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @GetMapping()
    public SampleModel sampleGetEndpoint(@RequestParam String name){
        SampleModel newModel = new SampleModel();
        newModel.Id = UUID.randomUUID();
        newModel.Name = name;

        return newModel;
    }
}
