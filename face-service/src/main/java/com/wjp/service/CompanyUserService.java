package com.wjp.service;

import bean.dto.EmployeeDTO;
import bean.dto.EmployeeSearchDTO;
import bean.vo.EmployeeVO;
import bean.vo.PageResult;

public interface CompanyUserService {
    void createUser(EmployeeDTO employeeDTO);

    void updateCompany(Integer employeeId, Integer role);

    void deleteCompany(Integer employeeId, Integer companyId);

    PageResult<EmployeeVO> list(EmployeeSearchDTO employeeSearchDTO);
}
