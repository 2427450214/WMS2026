package org.example.wms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

/**
 * 仓库DTO
 */
@Data
public class WarehouseDTO {
    private Integer id;

    @NotNull(message = "仓库名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,20}$", message = "仓库名称只能为大小写英文、数字和中文，且长度不超过20个字符")
    private String warehouseName;

    private String warehouseCode;

    @NotNull(message = "仓库状态不能为空")
    @Pattern(regexp = "^(启用|弃用|维护|检查中)$", message = "仓库状态只能是启用、弃用、维护、检查中四种状态")
    private String status;

    @NotNull(message = "仓库类型不能为空")
    @Pattern(regexp = "^(冷藏|普通|危险品|临时)$", message = "仓库类型只能是冷藏、普通、危险品、临时四种")
    private String warehouseType;

    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;
}