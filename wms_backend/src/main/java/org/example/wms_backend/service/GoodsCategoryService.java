package org.example.wms_backend.service;

import org.example.wms_backend.dto.GoodsCategoryDTO;

import java.util.List;

/**
 * 货物分类表服务接口
 */
public interface GoodsCategoryService {
    /**
     * 新增商品分类
     * @param goodsCategoryDTO 商品分类信息
     * @return 操作结果
     */
    String save(GoodsCategoryDTO goodsCategoryDTO);
    
    /**
     * 查询所有商品分类
     * @return 商品分类列表
     */
    List<GoodsCategoryDTO> findAll();
    
    /**
     * 根据商品分类主键ID查询商品分类
     * @param id 商品分类主键ID
     * @return 商品分类信息
     */
    GoodsCategoryDTO findById(Integer id);
    
    /**
     * 根据商品分类名称查询
     * @param categoryName 商品分类名称
     * @return 商品分类列表
     */
    List<GoodsCategoryDTO> findByCategoryName(String categoryName);
    
    /**
     * 使用商品分类主键ID进行对分类名称的修改
     * @param goodsCategoryDTO 商品分类信息
     * @return 操作结果
     */
    String updateGoodsCategory(GoodsCategoryDTO goodsCategoryDTO);
    
    /**
     * 逻辑删除功能
     * @param id 商品分类主键ID
     * @param isDeleted 逻辑删除状态
     * @return 操作结果
     */
    String updateIsDeleted(Integer id, Integer isDeleted);

    /**
     * 分页查询商品分类
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsCategoryDTO> findByPage(int page, int pageSize);

    /**
     * 按分类名模糊查询分页
     * @param categoryName 分类名
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsCategoryDTO> findByCategoryNamePage(String categoryName, int page, int pageSize);
}

