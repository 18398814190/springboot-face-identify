package com.wjp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wjp
 * @since 2023-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyInfo extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String companyName;

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
     * 删除标记 1代表删除
     */
    private Integer deleteFlag;

    /**
     * 打卡时间限制
     */
    private Date limitTime;


}
