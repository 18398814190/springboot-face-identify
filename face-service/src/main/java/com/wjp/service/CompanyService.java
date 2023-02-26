package com.wjp.service;

import bean.dto.CompanyDTO;
import bean.vo.CompanyVO;
import bean.vo.PageResult;

import java.util.List;

public interface CompanyService {
    void createCompany(CompanyDTO companyDTO);


    void updateCompany(CompanyDTO companyDTO);

    boolean exist(String companyName);

    PageResult<CompanyVO> getCompanyList(Integer pageNum, Integer pageSize);
}
