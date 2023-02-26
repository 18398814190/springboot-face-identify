package com.wjp.service.impl;

import bean.dto.EmployeeDTO;
import bean.dto.EmployeeSearchDTO;
import bean.vo.EmployeeVO;
import bean.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjp.constant.FaceConstants;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.mapper.BaseUserMapper;
import com.wjp.mapper.CompanyInfoMapper;
import com.wjp.mapper.CompanyUserInfoMapper;
import com.wjp.mapper.UserInfoMapper;
import com.wjp.pojo.BaseUser;
import com.wjp.pojo.CompanyInfo;
import com.wjp.pojo.CompanyUserInfo;
import com.wjp.pojo.UserInfo;
import com.wjp.service.CompanyUserService;
import com.wjp.util.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyUserServiceImpl implements CompanyUserService {

    @Resource
    private BaseUserMapper baseUserMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private CompanyInfoMapper companyInfoMapper;
    @Resource
    private CompanyUserInfoMapper companyUserInfoMapper;

    @Override
    public void createUser(EmployeeDTO employeeDTO) {
        String companyName = employeeDTO.getCompanyName();
        String name = employeeDTO.getName();
        String phoneNumber = employeeDTO.getPhoneNumber();

        // 先判断BaseUser表是否存在该用户，不存在插入BaseUser表和UserInfo表
        LambdaQueryWrapper<BaseUser> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(BaseUser::getPhoneNumber, phoneNumber);
        BaseUser selectUser = baseUserMapper.selectOne(lqw1);

        LambdaQueryWrapper<CompanyInfo> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(CompanyInfo::getCompanyName, companyName);
        CompanyInfo companyInfo = companyInfoMapper.selectOne(lqw2);

        if (companyInfo == null) {
            throw new FaceException(FaceErrorEnum.COMPANY_NO_EXIST_EXCEPTION);
        }

        if (selectUser == null) {
            BaseUser baseUser = new BaseUser();
            baseUser.setPhoneNumber(phoneNumber);
            baseUserMapper.insert(baseUser);
            selectUser = baseUser;
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(baseUser.getId());
            userInfo.setNickName(name);
            userInfo.setPhoneNumber(phoneNumber);
            userInfoMapper.insert(userInfo);
        }

        LambdaQueryWrapper<CompanyUserInfo> lqw3 = new LambdaQueryWrapper<>();

        Integer userId = selectUser.getId();
        Integer companyId = companyInfo.getId();
        lqw3.eq(CompanyUserInfo::getCompanyId, companyId)
                .eq(CompanyUserInfo::getUserId, userId);
        if (companyUserInfoMapper.selectCount(lqw3) > 0) {
            throw new FaceException(FaceErrorEnum.EMPLOYEE_EXIST_EXCEPTION);
        }
        CompanyUserInfo companyUserInfo = new CompanyUserInfo();
        companyUserInfo.setUserId(userId);
        companyUserInfo.setCompanyId(companyId);
        companyUserInfo.setRole(employeeDTO.getRole());
        companyUserInfoMapper.insert(companyUserInfo);


    }

    @Override
    public void updateCompany(Integer employeeId, Integer role) {
        CompanyUserInfo companyUserInfo = new CompanyUserInfo();
        companyUserInfo.setRole(role);
        companyUserInfo.setId(employeeId);
        companyUserInfoMapper.updateById(companyUserInfo);

    }

    @Override
    public void deleteCompany(Integer employeeId, Integer companyId) {
        // 查询出该员工，权限为3则不可删除
        CompanyUserInfo companyUserInfo = companyUserInfoMapper.selectById(employeeId);

        // 判断自己当前账号的权限，只有权限为3才可删除员工
        int userId = ThreadLocalUtils.getUserId();
        LambdaQueryWrapper<CompanyUserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CompanyUserInfo::getCompanyId, companyId).eq(CompanyUserInfo::getUserId, userId);
        CompanyUserInfo selectOne = companyUserInfoMapper.selectOne(lqw);

        if (companyUserInfo.getRole().equals(FaceConstants.COMPANY_ROLE_BOSS)
                || !selectOne.getRole().equals(FaceConstants.COMPANY_ROLE_BOSS)) {
            throw new FaceException(FaceErrorEnum.ROLE_EXCEPTION);
        }

        companyUserInfoMapper.deleteById(employeeId);

    }

    @Override
    public PageResult<EmployeeVO> list(EmployeeSearchDTO employeeSearchDTO) {

        List<EmployeeVO> result = new ArrayList<>();
        // 1.先根据companyId查询
        Integer companyId = employeeSearchDTO.getCompanyId();
        Integer pageNum = employeeSearchDTO.getPageNum();
        Integer pageSize = employeeSearchDTO.getPageSize();
        if (pageNum == null && pageSize == null) {
            pageNum = 1;
            pageSize = 10;
        }
        String searchFlag = employeeSearchDTO.getSearchFlag();
        if (StringUtils.hasLength(searchFlag)) {
            LambdaQueryWrapper<CompanyUserInfo> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(CompanyUserInfo::getCompanyId, companyId);
            List<CompanyUserInfo> companyUserInfos = companyUserInfoMapper.selectList(lqw1);

            if (CollectionUtils.isEmpty(companyUserInfos)) {
                return new PageResult<>();
            }

            for (CompanyUserInfo companyUserInfo : companyUserInfos) {
                EmployeeVO employeeVO = new EmployeeVO();
                employeeVO.setId(companyUserInfo.getId());
                employeeVO.setRole(companyUserInfo.getRole());
                employeeVO.setFaceEntryFlag(companyUserInfo.getFaceEntryFlag());
                Integer userId = companyUserInfo.getUserId();
                LambdaQueryWrapper<UserInfo> lqw2 = new LambdaQueryWrapper<>();
                lqw2.eq(UserInfo::getUserId, userId);
                lqw2.and(qr -> qr.like(UserInfo::getPhoneNumber, searchFlag)
                        .or()
                        .like(UserInfo::getNickName, searchFlag));
                UserInfo userInfo = userInfoMapper.selectOne(lqw2);
                if (userInfo != null) {
                    employeeVO.setNickName(userInfo.getNickName());
                    employeeVO.setPhoneNumber(userInfo.getPhoneNumber());
                    employeeVO.setUserAge(userInfo.getUserAge());
                    employeeVO.setUserAvatar(userInfo.getUserAvatar());
                    result.add(employeeVO);
                }
            }
            // 处理后的所有符合条件的数据（list）
            List<EmployeeVO> collect = result.stream().skip((pageNum - 1) * pageSize)
                    .limit(pageSize).collect(Collectors.toList());


            return new PageResult<>(pageNum, pageSize, result.size(), collect);
        } else {
            Page<CompanyUserInfo> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<CompanyUserInfo> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(CompanyUserInfo::getCompanyId, companyId);
            companyUserInfoMapper.selectPage(page, lqw1);
            List<CompanyUserInfo> companyUserInfos = page.getRecords();
            if (CollectionUtils.isEmpty(companyUserInfos)) {
                return new PageResult<EmployeeVO>();
            }

            for (CompanyUserInfo companyUserInfo : companyUserInfos) {
                EmployeeVO employeeVO = new EmployeeVO();
                employeeVO.setId(companyUserInfo.getId());
                employeeVO.setRole(companyUserInfo.getRole());
                employeeVO.setFaceEntryFlag(companyUserInfo.getFaceEntryFlag());
                Integer userId = companyUserInfo.getUserId();


                LambdaQueryWrapper<UserInfo> lqw2 = new LambdaQueryWrapper<>();
                lqw2.eq(UserInfo::getUserId, userId);

                UserInfo userInfo = userInfoMapper.selectOne(lqw2);
                employeeVO.setPhoneNumber(userInfo.getPhoneNumber());
                employeeVO.setNickName(userInfo.getNickName());
                employeeVO.setUserAge(userInfo.getUserAge());
                employeeVO.setUserAvatar(userInfo.getUserAvatar());
                result.add(employeeVO);
            }
            // 在分别查询BaseUser和UserInfo表，组装
            return new PageResult<>(pageNum, pageSize, (int) page.getTotal(), result);
        }

    }
}
