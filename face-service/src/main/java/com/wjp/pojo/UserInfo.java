package com.wjp.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class UserInfo extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * base_user表主键
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 生日
     */
    private String userBirthday;

    /**
     * 年龄
     */
    private String userAge;

    /**
     * 用户头像
     */
    private String userAvatar;

}
