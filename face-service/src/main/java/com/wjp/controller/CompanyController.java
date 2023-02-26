package com.wjp.controller;

import bean.dto.CompanyDTO;
import bean.vo.CompanyVO;
import bean.vo.PageResult;
import bean.vo.ResultVO;
import com.wjp.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
     * 公司名是否存在查询
     *
     * @param companyName
     * @return
     */
    @PostMapping("/exist")
    public ResultVO<Boolean> createCompany(String companyName) {
        return ResultVO.success(companyService.exist(companyName));
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
    public ResultVO<PageResult<CompanyVO>> getCompanyList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageResult<CompanyVO> companyVO = companyService.getCompanyList(pageNum, pageSize);
        return ResultVO.success(companyVO);
    }

}
