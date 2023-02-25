package com.wjp.controller;

import bean.dto.CompanyDTO;
import bean.vo.CompanyVO;
import bean.vo.ResultVO;
import com.wjp.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 公司创建
     *
     * @param companyDTO
     * @return
     */
    @PostMapping("/create")
    public ResultVO createCompany(@RequestBody @Valid CompanyDTO companyDTO) {
        companyService.createCompany(companyDTO);
        return ResultVO.success(null);
    }

    /**
     * 公司信息修改
     *
     * @param companyDTO
     * @return
     */
    @PutMapping("/update")
    public ResultVO updateCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.updateCompany(companyDTO);
        return ResultVO.success(null);
    }


    /**
     * 根据用户ID查询所属公司
     *
     * @return
     */
    @GetMapping("/list")
    public ResultVO<List<CompanyVO>> getCompanyList() {
        List<CompanyVO> companyVO = companyService.getCompanyList();
        return ResultVO.success(companyVO);
    }

}
