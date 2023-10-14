package com.app.gamereview.controller;

import com.app.gamereview.model.SampleModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
/*
    @RestController annotation -> we ensure that Spring knows that this class is a controller class
    @RequestMapping() -> the string inside the annotation defines the base url of endpoints inside this controller class
    @GetMapping() -> can provide a string inside (e.g. "/user") which will define the url of that specific endpoint
    Ex: @GetMapping("/user) -> url of that endpoint will become " /api/sample/user"
    Also : PostMapping(), PutMapping() etc.
 */
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
