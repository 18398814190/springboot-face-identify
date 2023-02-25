package bean.vo;

import lombok.Data;

@Data
public class LoginVo {
    // 鉴权token
    private String token;

    // 新用户标记
    private Boolean userFlag;
}
