package com.wjp.service.impl;

import bean.dto.CompanyDTO;
import bean.vo.CompanyVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjp.autoconfig.template.BaiduTemplate;
import com.wjp.constant.FaceConstants;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.mapper.CompanyInfoMapper;
import com.wjp.mapper.CompanyUserInfoMapper;
import com.wjp.pojo.CompanyInfo;
import com.wjp.pojo.CompanyUserInfo;
import com.wjp.service.CompanyService;
import com.wjp.util.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private BaiduTemplate baiduTemplate;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private CompanyUserInfoMapper companyUserInfoMapper;

    @Override
    public void createCompany(CompanyDTO companyDTO) {
        int userId = ThreadLocalUtils.getUserId();
        CompanyInfo companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(companyDTO, companyInfo);
        companyInfo.setCompanyOwner(userId);
        // 根据经纬度获取地址
        if (StringUtils.isEmpty(companyInfo.getCompanyAddress())) {
            String companyCoordinate = companyDTO.getCompanyCoordinate();
            String[] split = companyCoordinate.split(",");
            String address = baiduTemplate.getAddressInfoByLngAndLat(split[0], split[1]);
            companyInfo.setCompanyAddress(address);
        }

        companyInfoMapper.insert(companyInfo);

        // 同时往company_user_info表插入一条信息
        CompanyUserInfo companyUserInfo = new CompanyUserInfo();
        companyUserInfo.setCompanyId(companyInfo.getId());
        companyUserInfo.setRole(FaceConstants.COMPANY_ROLE_BOSS);
        companyUserInfo.setUserId(companyInfo.getCompanyOwner());
        companyUserInfoMapper.insert(companyUserInfo);
    }

    @Override
    public List<CompanyVO> getCompanyList() {
        // 首先根据UserId查询CompanyUserInfo表，再去查出对应的list
        List<CompanyVO> companyVOS = new ArrayList<>();
        int userId = ThreadLocalUtils.getUserId();
        LambdaQueryWrapper<CompanyUserInfo> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(CompanyUserInfo::getUserId, userId);
        List<CompanyUserInfo> companyUserInfos = companyUserInfoMapper.selectList(lqw1);
        if (CollectionUtils.isEmpty(companyUserInfos)) {
            return companyVOS;
        }

        for (CompanyUserInfo companyUserInfo : companyUserInfos) {
            Integer role = companyUserInfo.getRole();
            CompanyVO companyVO = new CompanyVO();
            companyVO.setRole(getRoleName(role));
            CompanyInfo companyInfo = companyInfoMapper.selectById(companyUserInfo.getCompanyId());
            BeanUtils.copyProperties(companyInfo, companyVO);
            companyVOS.add(companyVO);
        }
        return companyVOS;
    }

    @Override
    public void updateCompany(CompanyDTO companyDTO) {
        checkRole(companyDTO);

        if (companyDTO.getDeleteFlag() != null && companyDTO.getDeleteFlag() == 1) {
            Integer companyId = companyDTO.getId();
            LambdaQueryWrapper<CompanyUserInfo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(CompanyUserInfo::getCompanyId, companyId);
            if (companyUserInfoMapper.selectCount(lqw) > 0) {
                throw new FaceException(FaceErrorEnum.DELETE_COMPANY_EXCEPTION);
            }
            companyInfoMapper.deleteById(companyId);
            return;
        }

        CompanyInfo update = new CompanyInfo();
        BeanUtils.copyProperties(companyDTO, update);
        if (StringUtils.isEmpty(companyDTO.getCompanyAddress())
                && StringUtils.isNotEmpty(companyDTO.getCompanyCoordinate())) {
            String companyCoordinate = companyDTO.getCompanyCoordinate();
            String[] split = companyCoordinate.split(",");
            String address = baiduTemplate.getAddressInfoByLngAndLat(split[0], split[1]);
            update.setCompanyAddress(address);
        }

        companyInfoMapper.updateById(update);
    }

    @Override
    public boolean exist(String companyName) {
        LambdaQueryWrapper<CompanyInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CompanyInfo::getCompanyName, companyName);
        if (companyInfoMapper.selectCount(lqw) > 0) {
            return true;
        }
        return false;
    }

    private void checkRole(CompanyDTO companyDTO) {
        int userId = ThreadLocalUtils.getUserId();
        Integer id = companyDTO.getId();
        CompanyInfo companyInfo = companyInfoMapper.selectById(id);
        if (companyInfo.getCompanyOwner() != userId) {
            throw new FaceException(FaceErrorEnum.ROLE_EXCEPTION);
        }
    }

    private String getRoleName(Integer role) {
        return FaceConstants.COMPANY_ROLE_BOSS.equals(role) ? "经营者"
                : FaceConstants.COMPANY_ROLE_MANAGE.equals(role) ? "管理者" : "员工";
    }
}
