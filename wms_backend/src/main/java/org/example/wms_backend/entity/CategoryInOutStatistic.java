
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class CategoryInOutStatistic {
    private Integer categoryId;
    private String categoryName;
    private Integer totalInbound;
    private Integer totalOutbound;
}

