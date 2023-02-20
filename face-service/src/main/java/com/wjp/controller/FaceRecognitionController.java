package com.wjp.controller;

import bean.ResultVO;
import com.alibaba.fastjson.JSONObject;
import com.wjp.autoconfig.template.FaceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/face")
public class FaceRecognitionController {

    @Autowired
    private FaceTemplate faceTemplate;

    @GetMapping
    public ResultVO<JSONObject> test() throws IOException {
        String detect = faceTemplate.detect("C:\\Users\\17297\\Desktop\\test.jpg");

        return ResultVO.success(JSONObject.parseObject(detect));
    }

    /**
     * 解析人脸图片，获取face_token
     * @return
     * @throws IOException
     */
    @PostMapping("/detect")
    public ResultVO<JSONObject> detect(MultipartFile file) throws IOException {
        String detect = faceTemplate.detectFile(MultipartFileToFile(file));

        return ResultVO.success(JSONObject.parseObject(detect));
    }

    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
