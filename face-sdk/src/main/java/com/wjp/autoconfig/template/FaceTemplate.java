package com.wjp.autoconfig.template;

import com.alibaba.fastjson.JSONObject;
import com.wjp.autoconfig.properties.FaceProperties;
import com.wjp.autoconfig.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FaceTemplate {

    private FaceProperties faceProperties;

    /**
     * 人脸识别
     */
    private static final String DETECT_URL = "/detect";

    /**
     * 创建FaceSet
     */
    private static final String CREATE_FACESET_URL = "/faceset/create";

    /**
     * 人脸搜索
     */
    private static final String SEARCH_FACE_URL = "/search";

    /**
     * 删除FaceSet
     */
    private static final String DELETE_FACESET_URL = "/faceset/delete";

    /**
     * 获取FaceSet列表信息
     */
    private static final String GET_FACESET_List_URL = "/faceset/getfacesets";

    /**
     * 获取指定FaceSet信息
     */
    private static final String GET_FACESET_URL = "/faceset/getdetail";

    /**
     * 添加人脸
     */
    private static final String ADD_FACE_URL = "/faceset/addface";

    /**
     * 删除人脸
     */
    private static final String DALETE_FACE_URL = "/faceset/removeface";

    public FaceTemplate(FaceProperties faceProperties) {
        this.faceProperties = faceProperties;
    }

    public String detect(String imageFile) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("image_base64", fileToBase64(imageFile));
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + DETECT_URL, params, null);
        return result;
    }


    public String detectFile(File file) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("image_base64", fileToBase64(file));
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + DETECT_URL, params, null);
        return result;
    }

    /**
     *
     * @param outerId faceSet唯一标识
     * @return
     * @throws IOException
     */
    public boolean createFaceSet(String outerId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        // faceSet唯一标识
        params.put("outer_id", StringUtils.isNotEmpty(outerId) ? outerId : "wjpTest");
        params.put("tag", "FaceSetGroup");
        params.put("force_merge", "1");
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + CREATE_FACESET_URL, params, null);
        log.info("createFaceSet success, result : {}", result);
        return true;
    }

    /**
     * 删除faceSet
     * @param outerId
     * @return
     * @throws IOException
     */
    public boolean deleteFaceSetNotCheckEmpty(String outerId) throws IOException {
        if (StringUtils.isEmpty(outerId)) {
            log.error("deleteFaceSet fail, outerId is null !");
            return false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        // faceSet唯一标识
        params.put("outer_id", outerId);
        params.put("check_empty", "0");
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + DELETE_FACESET_URL, params, null);
        log.info("deleteFaceSet success, result : {}", result);
        return true;
    }

    /**
     * 删除faceSet校验非空
     * @param outerId
     * @return
     * @throws IOException
     */
    public boolean deleteFaceSetCheckEmpty(String outerId) throws IOException {
        if (StringUtils.isEmpty(outerId)) {
            log.error("deleteFaceSet fail, outerId is null !");
            return false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        // faceSet唯一标识
        params.put("outer_id", outerId);
        params.put("check_empty", "1");
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + DELETE_FACESET_URL, params, null);
        log.info("deleteFaceSet success, result : {}", result);
        return true;
    }

    /**
     * 获取FaceSet集合
     * @param start 查询下标
     * @return
     * @throws IOException
     */
    public String getFaceSetList(Integer start) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("start", String.valueOf(start));
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + GET_FACESET_List_URL, params, null);
        log.info("getFaceSetList success, result : {}", result);
        return result;
    }

    /**
     * 获取指定FaceSet
     * @param outerId 查询下标
     * @return
     * @throws IOException
     */
    public String getFaceSet(String outerId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("outer_id", outerId);
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + GET_FACESET_URL, params, null);
        log.info("getFaceSetList success, result : {}", result);
        return result;
    }

    /**
     * 添加face
     * @param outerId 查询下标
     * @return
     * @throws IOException
     */
    public boolean addFace(String outerId, String faceToken) throws IOException {
        if (StringUtils.isEmpty(outerId) || StringUtils.isEmpty(faceToken)) {
            return false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("outer_id", outerId);
        // 人脸标识 face_token 组成的字符串，可以是一个或者多个，用逗号分隔。最多不超过5个face_token
        params.put("face_tokens", faceToken);
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + ADD_FACE_URL, params, null);
        log.info("add Face success, result : {}", result);
        return true;
    }

    /**
     * 删除face
     * @param outerId 查询下标
     * @return
     * @throws IOException
     */
    public boolean deleteFace(String outerId, String faceToken) throws IOException {
        if (StringUtils.isEmpty(outerId) || StringUtils.isEmpty(faceToken)) {
            return false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        params.put("outer_id", outerId);
        // 需要移除的人脸标识字符串，可以是一个或者多个face_token组成，用逗号分隔。最多不能超过1,000个face_token
        //注：face_tokens字符串传入“RemoveAllFaceTokens”则会移除FaceSet内所有的face_token
        params.put("face_tokens", faceToken);
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + DALETE_FACE_URL, params, null);
        log.info("add Face success, result : {}", result);
        return true;
    }

    /**
     * 查询face
     * @param outerId 查询下标
     * @return
     * @throws IOException
     */
    public boolean searchFace(String outerId, String faceToken) throws IOException {
        if (StringUtils.isEmpty(outerId) || StringUtils.isEmpty(faceToken)) {
            return false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("api_key", faceProperties.getApiKey());
        params.put("api_secret", faceProperties.getApiSecret());
        // 用户自定义的 FaceSet 标识
        params.put("outer_id", outerId);
        // 进行搜索的目标人脸的 face_token，优先使用该参数
        params.put("face_token", faceToken);
        String result = HttpUtils.doPost(faceProperties.getBaseUrl() + SEARCH_FACE_URL, params, null);
        log.info("search Face success, result : {}", result);
        JSONObject resultJson = JSONObject.parseObject(result);
        Integer confidence = resultJson.getJSONArray("results").getJSONObject(0).getInteger("confidence");
        // 相似度为85以上则判断为同一个人
        if (confidence > faceProperties.getConfidence()) {
            return true;
        }
        return false;
    }


    /**
     * @Description: 文件转为base64字符串。filePath：文件路径
     * @Param: [filePath]
     * @return: java.lang.String
     * @Date: 2020/12/25
     */
    private String fileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        return fileToBase64(file);
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