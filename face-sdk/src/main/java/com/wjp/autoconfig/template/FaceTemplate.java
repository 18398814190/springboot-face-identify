package com.wjp.autoconfig.template;

import com.alibaba.fastjson.JSONObject;
import com.wjp.autoconfig.properties.FaceProperties;

import java.io.File;

public class FaceTemplate {

    private FaceProperties faceProperties;

    public FaceTemplate(FaceProperties faceProperties){
        this.faceProperties = faceProperties;
    }

    public JSONObject detect(File file){

        return new JSONObject();
    }
}