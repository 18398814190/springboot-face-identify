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
public class UserInfo extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * base_user表主键
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 生日
     */
    private String userBirthday;

    /**
     * 年龄
     */
    private Integer userAge;

    /**
     * 用户头像
     */
    private String userAvatar;

}
