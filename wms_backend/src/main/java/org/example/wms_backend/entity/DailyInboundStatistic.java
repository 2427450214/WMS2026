package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class DailyInboundStatistic {
    private String date;
    private Integer quantity;
}
