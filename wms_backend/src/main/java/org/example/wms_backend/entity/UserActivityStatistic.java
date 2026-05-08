
package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class UserActivityStatistic {
    private String account;
    private String userName;
    private Integer operationCount;
}

