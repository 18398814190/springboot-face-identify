package bean.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 生日
     */
    private String userBirthday;

    /**
     * 用户头像
     */
    private String userAvatar;
}
