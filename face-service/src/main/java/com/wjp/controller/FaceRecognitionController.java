package com.wjp.controller;

import com.wjp.autoconfig.template.FaceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face")
public class FaceRecognitionController {

    @Autowired
    private FaceTemplate faceTemplate;

    @GetMapping
    public String test(){
        faceTemplate.detect(null);
        return "成功";
    }

}
