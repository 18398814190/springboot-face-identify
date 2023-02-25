package bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {
    @NotNull
    private String phone;

    @NotNull
    private String verificationCode;
}
