package bean.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyVO {
    /**
     * 名称
     */
    private String companyName;


    /**
     * 图标
     */
    private String companyIcon;


    /**
     * 所属人
     */
    private Integer companyOwner;

    /**
     * 描述
     */
    private String companyDescribe;

    /**
     * 地址
     */
    private String companyAddress;

    /**
     * 打卡时间限制
     */
    private Date limitTime;

    /**
     * 公司职位
     */
    private String role;
}
