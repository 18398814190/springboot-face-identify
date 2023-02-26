package bean.dto;

import lombok.Data;

@Data
public class EmployeeSearchDTO extends PageDTO{

    private Integer companyId;

    private String searchFlag;
}
