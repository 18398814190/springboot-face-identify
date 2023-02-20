package com.wjp.autoconfig.template;

import com.wjp.autoconfig.properties.FaceProperties;
import com.wjp.autoconfig.util.HttpUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FaceTemplate {

    private FaceProperties faceProperties;

    public FaceTemplate(FaceProperties faceProperties) {
        this.faceProperties = faceProperties;
    }

    public String detect(String imageFile) throws IOException {
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("image_base64", fileToBase64(imageFile));
        String result = HttpUtils.doPost(url, params, null);
        return result;
    }


    public String detectFile(File file) throws IOException {
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("image_base64", fileToBase64(file));
        String result = HttpUtils.doPost(url, params, null);
        return result;
    }


    /**
     * @Description: 文件转为base64字符串。filePath：文件路径
     * @Param: [filePath]
     * @return: java.lang.String
     * @Date: 2020/12/25
     */
    private String fileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream inputFile = null;
        byte[] buffer = null;
        try {
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputFile) {
                inputFile.close();
            }
        }
        byte[] bs = Base64.encodeBase64(buffer);
        return new String(bs);
    }

    /**
     * @Description: 文件转为base64字符串。filePath：文件路径
     * @Param: [filePath]
     * @return: java.lang.String
     * @Date: 2020/12/25
     */
    private String fileToBase64(File file) throws IOException {
        FileInputStream inputFile = null;
        byte[] buffer = null;
        try {
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputFile) {
                inputFile.close();
            }
        }
        byte[] bs = Base64.encodeBase64(buffer);
        return new String(bs);
    }
}