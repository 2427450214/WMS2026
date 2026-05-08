package org.example.wms_backend.dto;

import lombok.Data;

/**
 * 分页查询请求DTO
 */
@Data
public class PageRequestDTO {
    /**
     * 页码，默认为1
     */
    private int page = 1;

    /**
     * 每页数量，默认为10
     */
    private int pageSize = 10;
}
