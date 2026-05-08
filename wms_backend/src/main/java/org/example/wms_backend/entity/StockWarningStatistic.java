
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class StockWarningStatistic {
    private String goodsCode;
    private String goodsName;
    private Integer stockQuantity;
    private Integer minStockWarning;
    private Integer maxStockWarning;
    private String warningType;
}

