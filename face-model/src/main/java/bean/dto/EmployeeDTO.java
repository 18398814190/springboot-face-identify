package bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmployeeDTO {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String name;
    @NotNull
    private String companyName;

    private Integer role;

}
