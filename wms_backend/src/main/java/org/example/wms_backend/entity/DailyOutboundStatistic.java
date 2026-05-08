package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class DailyOutboundStatistic {
    private String date;
    private Integer quantity;
}
