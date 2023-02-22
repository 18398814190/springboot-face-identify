package com.wjp.controller;

import bean.ResultVO;
import com.wjp.autoconfig.template.FaceTemplate;
import com.wjp.service.FaceRecognitionService;
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

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @GetMapping
    public ResultVO<Object> test() throws IOException {
        // 解析人脸图片
        // String detect = faceTemplate.detect("C:\\Users\\17297\\Desktop\\test.jpg");
        // 创建FaceSet
        // boolean detect = faceTemplate.createFaceSet(null);

        // 删除FaceSet
        // boolean detect = faceTemplate.deleteFaceSetNotCheckEmpty("wjpTest");

        // 获取FaceSet集合
        // String detect = faceTemplate.getFaceSetList(1);

        // 获取指定FaceSet
        //String detect = faceTemplate.getFaceSet("wjpTest");

        // 整套人脸校验逻辑 添加人脸 -> 校验人脸(true) ->删除人脸 -> 校验人脸（faceSet为空则异常）
/*      String detect1 = faceTemplate.detect("C:\\Users\\17297\\Desktop\\test.jpg");
        String faceToken = faceRecognitionService.getFaceToken(detect1);
        boolean addFace = faceTemplate.addFace("wjpTest", faceToken);
        boolean wjpTest = faceTemplate.searchFace("wjpTest", faceToken);
        faceTemplate.deleteFace("wjpTest", faceToken);
        boolean wjpTest2 = faceTemplate.searchFace("wjpTest", faceToken);
        return ResultVO.success(wjpTest2);*/

        return null;
    }

    /**
     * 解析人脸图片，获取face_token
     * @return
     * @throws IOException
     */
    @PostMapping("/detect")
    public ResultVO<String> detect(MultipartFile file) throws IOException {
        String detect = faceTemplate.detectFile(MultipartFileToFile(file));
        // 获取faceToken
        String face_token = faceRecognitionService.getFaceToken(detect);
        return ResultVO.success(face_token);
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
