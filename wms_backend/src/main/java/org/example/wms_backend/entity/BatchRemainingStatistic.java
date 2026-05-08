
package org.example.wms_backend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BatchRemainingStatistic {
    private String batchNo;
    private String goodsCode;
    private String goodsName;
    private Integer batchQuantity;
    private Integer remainingQuantity;
    private Date expiryDate;
}

