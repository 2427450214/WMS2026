
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class CategoryStockStatistic {
    private Integer categoryId;
    private String categoryName;
    private Integer totalStock;
}

