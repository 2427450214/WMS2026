package org.example.wms_backend.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.*;

@Data
public class UserDTO {
    private Integer id;
    
    @NotNull(message = "账号不能为空")
    @Pattern(regexp = "^\\d{8,12}$", message = "账号只能为8到12位纯数字")
    private String account;
    
    @NotNull(message = "姓名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z]{2,10}$", message = "姓名只能是二到10位的大小写英文或者纯中文")
    private String name;
    
    @NotNull(message = "密码不能为空")
    @JsonIgnore
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "密码必须为8到16位大小写英文加数字加特殊字符的组合")
    private String password;
    
    private Integer permissionLevel;
    
    private String accountStatus;
    private Integer isDeleted;
    
    private String inviteCode;
}