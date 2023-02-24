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
public class BaseUser extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String passWord;


}
