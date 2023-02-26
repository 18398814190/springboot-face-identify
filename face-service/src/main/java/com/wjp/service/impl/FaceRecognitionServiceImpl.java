package com.wjp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjp.autoconfig.template.FaceTemplate;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.mapper.CompanyInfoMapper;
import com.wjp.mapper.CompanyUserInfoMapper;
import com.wjp.pojo.CompanyInfo;
import com.wjp.pojo.CompanyUserInfo;
import com.wjp.service.FaceRecognitionService;
import com.wjp.util.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {
    @Resource
    private CompanyUserInfoMapper companyUserInfoMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Autowired
    private FaceTemplate faceTemplate;



    @Override
    public String getFaceToken(String detect) {
        JSONObject faceResult = JSONObject.parseObject(detect);
        if (faceResult.getInteger("face_num") > 1) {
            throw new FaceException("face_num can not big than 1", FaceErrorEnum.FACE_NUM_EXCEPTION);
        }
        JSONArray faces = faceResult.getJSONArray("faces");
        return faces.getJSONObject(0).getString("face_token");
    }

    /**
     *  人脸录入
     * @param faceToken
     * @param companyId
     */
    @Override
    public void faceEntry(String faceToken, String companyId) {
        // 此处不用判断该员工是否是公司员工了，但是需要判断是否已经录入人脸
        int userId = ThreadLocalUtils.getUserId();
        LambdaQueryWrapper<CompanyUserInfo> lqw = new LambdaQueryWrapper<>();

        lqw.eq(CompanyUserInfo::getCompanyId, companyId).eq(CompanyUserInfo::getUserId, userId);

        CompanyUserInfo companyUserInfo = companyUserInfoMapper.selectOne(lqw);
        if (companyUserInfo.getFaceEntryFlag() != 0) {
            throw new FaceException(FaceErrorEnum.FACE_ENTRY_EXCEPTION);
        }
        CompanyInfo companyInfo = companyInfoMapper.selectById(companyId);
        String companyName = companyInfo.getCompanyName();

        try {
            faceTemplate.addFace(companyName, faceToken);
        } catch (IOException e) {
            throw new FaceException(FaceErrorEnum.FACE_NUM_EXCEPTION);
        }
    }
}
