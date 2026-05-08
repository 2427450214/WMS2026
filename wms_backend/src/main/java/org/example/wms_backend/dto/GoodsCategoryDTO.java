package org.example.wms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 商品分类DTO
 */
@Data
public class GoodsCategoryDTO {
    private Integer id;

    @NotNull(message = "分类名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z]{2,10}$", message = "分类名称只能是二到10位的大小写英文或者纯中文")
    private String categoryName;

    private Integer isDeleted;
}