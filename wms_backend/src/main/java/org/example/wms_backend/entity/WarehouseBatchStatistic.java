
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class WarehouseBatchStatistic {
    private String warehouseCode;
    private String warehouseName;
    private Integer batchCount;
}

