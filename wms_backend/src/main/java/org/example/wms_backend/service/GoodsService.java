package org.example.wms_backend.service;

import org.example.wms_backend.dto.GoodsDTO;

import java.util.List;

/**
 * 货物表服务接口
 */
public interface GoodsService {
    /**
     * 新增货物
     * @param goodsDTO 货物DTO
     * @return 操作结果
     */
    String save(GoodsDTO goodsDTO);

    /**
     * 根据商品名查找货物（模糊查询）
     * @param goodsName 商品名
     * @return 货物DTO列表
     */
    List<GoodsDTO> findByGoodsName(String goodsName);
    
    /**
     * 查询所有商品
     * @return 货物DTO列表
     */
    List<GoodsDTO> findAll();
    
    /**
     * 根据商品编码查询商品
     * @param goodsCode 商品编码
     * @return 货物DTO
     */
    GoodsDTO findByGoodsCode(String goodsCode);
    
    /**
     * 根据分类查询商品
     * @param categoryId 分类ID
     * @return 货物DTO列表
     */
    List<GoodsDTO> findByCategoryId(Integer categoryId);
    
    /**
     * 修改商品信息
     * @param goodsDTO 货物DTO
     * @return 操作结果
     */
    String updateGoodsInfo(GoodsDTO goodsDTO);
    

    
    /**
     * 修改商品逻辑删除状态
     * @param goodsCode 商品编码
     * @param isDeleted 逻辑删除状态
     * @return 操作结果
     */
    String updateIsDeleted(String goodsCode, Integer isDeleted);
    
    /**
     * 根据库存数量排序查询商品
     * @param asc 是否升序
     * @return 货物DTO列表
     */
    List<GoodsDTO> findOrderByStockQuantity(boolean asc);
    
    /**
     * 查询库存量接近最低库存预警线的商品
     * @return 货物DTO列表
     */
    List<GoodsDTO> findNearMinStockWarning();
    
    /**
     * 根据库存接近最高库存线的商品
     * @return 货物DTO列表
     */
    List<GoodsDTO> findNearMaxStockWarning();

    /**
     * 根据商品名查找商品编码（模糊查询）
     * @param goodsName 商品名
     * @return 商品编码列表
     */
    List<String> findCodeByGoodsName(String goodsName);
    
    /**
     * 检查商品是否接近最大库存量
     * @param goodsCode 商品编码
     * @return 是否接近最大库存量
     */
    boolean isNearMaxStock(String goodsCode);

    /**
     * 查询库存数量最多的前N个商品
     * @param limit 限制数量
     * @return 货物DTO列表
     */
    List<org.example.wms_backend.dto.GoodsDTO> findTopStockGoods(int limit);

    /**
     * 分页查询商品
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsDTO> findByPage(int page, int pageSize);

    /**
     * 按商品名模糊查询分页
     * @param goodsName 商品名
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsDTO> findByGoodsNamePage(String goodsName, int page, int pageSize);

    /**
     * 按分类查询分页
     * @param categoryId 分类ID
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsDTO> findByCategoryIdPage(Integer categoryId, int page, int pageSize);
}
