package bean.vo;

import lombok.Data;

@Data
public class EmployeeVO {

    private Integer id;

    private Integer role;

    /**
     *  人脸录入标记 0未录入 1已录入
     */
    private Integer faceEntryFlag;


    private String phoneNumber;

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
    private Integer userAge;

    /**
     * 用户头像
     */
    private String userAvatar;
}
