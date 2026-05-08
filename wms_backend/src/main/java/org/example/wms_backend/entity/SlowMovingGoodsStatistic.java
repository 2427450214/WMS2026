
package org.example.wms_backend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SlowMovingGoodsStatistic {
    private String goodsCode;
    private String goodsName;
    private Integer stockQuantity;
    private Date lastOutboundTime;
}

