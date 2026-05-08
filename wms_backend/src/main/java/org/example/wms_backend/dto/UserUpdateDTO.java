package org.example.wms_backend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDTO {
    
    @Pattern(regexp = "^\\d{8,12}$", message = "账号只能为8到12位纯数字")
    private String account;
    
    private String oldPassword;
    
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z]{2,10}$", message = "姓名只能是二到10位的大小写英文或者纯中文")
    private String name;
    
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "密码必须为8到16位大小写英文加数字加特殊字符的组合")
    private String newPassword;
}
