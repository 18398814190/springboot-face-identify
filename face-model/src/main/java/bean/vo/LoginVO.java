package bean.vo;

import lombok.Data;

@Data
public class LoginVO {
    // 鉴权token
    private String token;

    // 新用户标记
    private Boolean userFlag;
}
