package org.example.wms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 货物表DTO
 */
@Data
public class GoodsDTO {
    /**
     * 货物ID
     */
    private Integer id;

    /**
     * 货物名称
     */
    @NotNull(message = "商品名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z]{1,10}$", message = "商品名称只能是1-10位的大小写英文或者中文")
    private String goodsName;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 货物分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Integer categoryId;

    /**
     * 库存数量
     */
    @Min(value = 0, message = "库存数量不能为负数")
    @Max(value = 99999, message = "库存数量不能超过99999")
    private Integer stockQuantity;

    /**
     * 保质期
     */
    @Min(value = 0, message = "保质期不能为负数")
    private Integer shelfLife;

    /**
     * 最低库存预警线
     */
    @Min(value = 1, message = "最低库存预警线必须大于0")
    private Integer minStockAlert;

    /**
     * 最高库存预警线
     */
    @Min(value = 1, message = "最高库存预警线必须大于0")
    @Max(value = 99999, message = "最高库存预警线不能超过99999")
    private Integer maxStockAlert;
}