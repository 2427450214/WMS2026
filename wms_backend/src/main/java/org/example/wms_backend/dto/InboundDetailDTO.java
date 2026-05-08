package org.example.wms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

/**
 * 入库详情表DTO
 */
@Data
public class InboundDetailDTO {
    /**
     * 入库详情ID
     */
    private Integer id;

    /**
     * 入库单ID
     */
    private String inboundId;

    /**
     * 商品编码
     */
    @NotNull(message = "商品编码不能为空")
    @Pattern(regexp = "^SP[a-zA-Z0-9]{8}$", message = "商品编码格式不正确，必须为SP+8位大小写英文与数字混合字符串")
    private String goodsCode;

    /**
     * 入库数量
     */
    @NotNull(message = "入库数量不能为空")
    @Min(value = 0, message = "入库数量不能为负数")
    @Max(value = 99999, message = "入库数量不能超过99999")
    private Integer inboundQuantity;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 仓库编码
     */
    @NotNull(message = "仓库编码不能为空")
    @Pattern(regexp = "^WH[a-zA-Z0-9]{8,18}$", message = "仓库编码格式不正确，必须为WH+8-18位大小写英文与数字混合字符串")
    private String warehouseCode;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 修改时间
     */
    private Date updateTime;
}
