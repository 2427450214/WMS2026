package org.example.wms_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存变动日志表DTO
 */
@Data
public class StockChangeLogDTO {
    /**
     * 库存变动日志ID
     */
    private Long id;

    /**
     * 商品编号
     */
    private String goodsCode;

    /**
     * 仓库编号
     */
    private String warehouseCode;

    /**
     * 变动前数量
     */
    private Integer beforeQuantity;

    /**
     * 变动数量
     */
    private Integer changeQuantity;

    /**
     * 变动后数量
     */
    private Integer afterQuantity;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 出入库详细表编码
     */
    private String inOutDetailCode;
    /**
     * 账号
     */
    private String account;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}
