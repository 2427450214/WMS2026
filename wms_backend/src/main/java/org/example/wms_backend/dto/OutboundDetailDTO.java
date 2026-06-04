package org.example.wms_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.Date;

/**
 * 出库详情表DTO
 */
@Data
public class OutboundDetailDTO {
    /**
     * 出库详情ID
     */
    private Integer id;

    /**
     * 出库单ID
     */
    private String outboundId;

    /**
     * 商品编码
     */
    @NotNull(message = "商品编码不能为空")
    @Pattern(regexp = "^SP[a-zA-Z0-9]{8}$", message = "商品编码格式不正确")
    private String goodsCode;

    /**
     * 出库数量
     */
    @NotNull(message = "出库数量不能为空")
    @Min(1) @Max(99999)
    private Integer outboundQuantity;

    /**
     * 批次号
     */
    @NotNull(message = "批次号不能为空")
    @Pattern(regexp = "^LOT[a-zA-Z0-9]{3}\\d{6}$", message = "批次号格式不正确，应为LOT+3位字符+6位日期(如LOT5Ep260429)")
    private String batchNo;

    /**
     * 仓库编码
     */
    @NotNull(message = "仓库编码不能为空")
    @Pattern(regexp = "^WH[a-zA-Z0-9]{8,18}$", message = "仓库编码格式不正确")
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
