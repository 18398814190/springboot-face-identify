package com.wjp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class CompanyUserInfo extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限 1普通员工 2管理 3老板
     */
    private Integer role;

    /**
     *  人脸录入标记 0未录入 1已录入
     */
    private Integer faceEntryFlag;

    private Integer userId;

    private Integer companyId;


}
