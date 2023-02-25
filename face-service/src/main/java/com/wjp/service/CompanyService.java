package com.wjp.service;

import bean.dto.CompanyDTO;
import bean.vo.CompanyVO;

import java.util.List;

public interface CompanyService {
    void createCompany(CompanyDTO companyDTO);

    List<CompanyVO> getCompanyList();

    void updateCompany(CompanyDTO companyDTO);

    boolean exist(String companyName);
}
