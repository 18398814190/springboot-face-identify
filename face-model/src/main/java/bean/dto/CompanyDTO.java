package bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CompanyDTO {

    private Integer id;
    /**
     * 名称
     */
    @NotNull
    private String companyName;

    /**
     * 图标
     */
    private String companyIcon;

    /**
     * 所属人
     */
    private String companyOwner;

    /**
     * 描述
     */
    private String companyDescribe;

    /**
     * 地址
     */
    private String companyAddress;

    /**
     * 经纬度
     */
    @NotNull
    private String companyCoordinate;

    private Integer deleteFlag;
}
