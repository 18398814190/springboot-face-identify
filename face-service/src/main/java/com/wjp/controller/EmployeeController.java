package com.wjp.controller;

import bean.dto.EmployeeDTO;
import bean.dto.EmployeeSearchDTO;
import bean.vo.EmployeeVO;
import bean.vo.PageResult;
import bean.vo.ResultVO;
import com.wjp.service.CompanyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Resource
    private CompanyUserService companyUserService;


    /**
     * 员工创建
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping("/create")
    public ResultVO createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        companyUserService.createUser(employeeDTO);
        return ResultVO.success(null);
    }

    /**
     * 员工权限修改
     * @param employeeId
     * @param role
     * @return
     */
    @PutMapping("/update")
    public ResultVO updateCompany(Integer employeeId, Integer role) {
        companyUserService.updateCompany(employeeId, role);
        return ResultVO.success(null);
    }

    /**
     * 员工删除
     * @param employeeId
     * @return
     */
    @DeleteMapping("/delete")
    public ResultVO deleteCompany(Integer employeeId, Integer companyId) {
        companyUserService.deleteCompany(employeeId, companyId);
        return ResultVO.success(null);
    }

    /**
     * 员工查询
     * @param employeeSearchDTO
     * @return
     */
    @GetMapping("/list")
    public ResultVO<PageResult<EmployeeVO>> list(EmployeeSearchDTO employeeSearchDTO) {
        PageResult<EmployeeVO> employeeVOList = companyUserService.list(employeeSearchDTO);
        return ResultVO.success(employeeVOList);
    }

}
