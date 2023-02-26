package com.wjp.controller;

import bean.vo.ResultVO;
import com.wjp.autoconfig.template.BaiduTemplate;
import com.wjp.autoconfig.template.FaceTemplate;
import com.wjp.service.FaceRecognitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/face")
@Slf4j
public class FaceRecognitionController {

    @Autowired
    private FaceTemplate faceTemplate;


    @Autowired
    private FaceRecognitionService faceRecognitionService;


    @Value("${tempFile}")
    private String tempFileDir;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Resource
    private BaiduTemplate baiduTemplate;

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
        //redisTemplate.opsForValue().set("wjp", "1234564");
        //smsTemplate.sendSms("172979", "18398814190")

/*        String addressInfoByLngAndLat = baiduTemplate.getAddressInfoByLngAndLat("114.081391", "22.626695");
        Map<String, Double> map = baiduTemplate.getLngAndLat(addressInfoByLngAndLat);
        double distance = baiduTemplate.getDistance(map.get("jd"), map.get("wd"), 114.081391, 22.626695);*/
        return ResultVO.success(null);
    }

    /**
     * 解析人脸图片，获取face_token
     * @return
     * @throws IOException
     */
    @PostMapping("/detect")
    public ResultVO<String> detect(MultipartFile file) throws IOException {
        String face_token;
        File faceFile = null;
        try {
            faceFile = MultipartFileToFile(file);
            String detect = faceTemplate.detectFile(faceFile);
            // 获取faceToken
            face_token = faceRecognitionService.getFaceToken(detect);
        } finally {
            if (faceFile != null) {
                faceFile.delete();
            }
        }
        return ResultVO.success(face_token);
    }

    /**
     * 人脸录入到指定公司
     * @return
     * @throws IOException
     */
    @PostMapping("/entry")
    public ResultVO faceEntry(MultipartFile file, String companyId) throws IOException {
        File faceFile = null;
        try {
            faceFile = MultipartFileToFile(file);
            String detect = faceTemplate.detectFile(faceFile);
            String faceToken = faceRecognitionService.getFaceToken(detect);
            // 获取faceToken
            faceRecognitionService.faceEntry(faceToken, companyId);
        } finally {
            if (faceFile != null) {
                faceFile.delete();
            }
        }
        return ResultVO.success(null);
    }


    public File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File fileDir = new File(tempFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = File.createTempFile(fileName, prefix, fileDir);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
        }
        return null;
    }

}
