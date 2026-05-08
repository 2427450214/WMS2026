
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class StockTurnoverStatistic {
    private String goodsCode;
    private String goodsName;
    private Integer totalOutbound;
    private Integer avgStock;
    private Double turnoverRate;
}

