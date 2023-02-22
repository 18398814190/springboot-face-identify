package com.wjp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.service.FaceRecognitionService;
import org.springframework.stereotype.Service;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    @Override
    public String getFaceToken(String detect) {
        JSONObject faceResult = JSONObject.parseObject(detect);
        if (faceResult.getInteger("face_num") > 1) {
            throw new FaceException("face_num can not big than 1", FaceErrorEnum.FACE_NUM_EXCEPTION);
        }
        JSONArray faces = faceResult.getJSONArray("faces");
        return faces.getJSONObject(0).getString("face_token");
    }
}
