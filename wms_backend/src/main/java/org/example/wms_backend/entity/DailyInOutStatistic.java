
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class DailyInOutStatistic {
    private String date;
    private Integer totalInbound;
    private Integer totalOutbound;
}

