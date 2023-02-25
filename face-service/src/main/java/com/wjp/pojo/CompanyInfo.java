package com.wjp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
     * 经纬度
     */
    private String companyCoordinate;

    /**
     * 删除标记 1代表删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleteFlag;

    /**
     * 打卡时间限制
     */
    private Date limitTime;


}
