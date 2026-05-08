package org.example.wms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

/**
 * 批次表DTO
 */
@Data
public class BatchDTO {
    /**
     * 批次ID
     */
    private Integer id;

    /**
     * 批次号（系统生成，不允许用户输入）
     */
    private String batchNumber;

    /**
     * 商品编码
     */
    @NotNull(message = "商品编码不能为空")
    @Pattern(regexp = "^SP[a-zA-Z0-9]{8}$", message = "商品编码格式不正确，必须为SP+8位大小写英文与数字混合字符串")
    private String goodsCode;

    /**
     * 商品名称（用于展示）
     */
    private String goodsName;

    /**
     * 生产日期
     */
    @NotNull(message = "生产日期不能为空")
    private Date productionDate;

    /**
     * 到期日期
     */
    private Date expiryDate;

    /**
     * 批次数量
     */
    @NotNull(message = "批次数量不能为空")
    @Min(value = 0, message = "批次数量不能为负数")
    @Max(value = 99999, message = "批次数量不能超过99999")
    private Integer batchQuantity;

    /**
     * 剩余数量
     */
    private Integer remainingQuantity;

    /**
     * 仓库编码
     */
    @NotNull(message = "仓库编码不能为空")
    @Pattern(regexp = "^WH[a-zA-Z0-9]{8}$", message = "仓库编码格式不正确，必须为WH+8位大小写英文与数字混合字符串")
    private String warehouseCode;

    /**
     * 仓库名称（用于展示）
     */
    private String warehouseName;

    /**
     * 状态
     */
    @Pattern(regexp = "^(正常|过期)$", message = "状态只能是正常或过期")
    private String status;
}
