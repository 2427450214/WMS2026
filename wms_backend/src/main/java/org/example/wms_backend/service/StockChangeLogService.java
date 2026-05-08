package org.example.wms_backend.service;

import org.example.wms_backend.dto.StockChangeLogDTO;

import java.util.List;

/**
 * 库存变动日志表服务接口
 */
public interface StockChangeLogService {
    /**
     * 查询所有库存变动日志
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findAll();
    
    /**
     * 根据操作类型查找库存变动日志
     * @param operationType 操作类型
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findByOperationType(String operationType);
    
    /**
     * 根据仓库编号查找库存变动日志
     * @param warehouseCode 仓库编号
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findByWarehouseCode(String warehouseCode);
    
    /**
     * 根据用户名称查找库存变动日志
     * @param userName 用户名称
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findByUserName(String userName);
    
    /**
     * 根据商品编号查找库存变动日志
     * @param goodsCode 商品编号
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findByGoodsCode(String goodsCode);
    
    /**
     * 根据账号查找库存变动日志
     * @param account 账号
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findByAccount(String account);
    
    /**
     * 根据出入库详细表查找库存变动日志
     * @param inOutDetailCode 出入库详细表编码
     * @return 库存变动日志列表
     */
    List<StockChangeLogDTO> findByInOutDetailCode(String inOutDetailCode);
    
    /**
     * 根据主键查找库存变动日志
     * @param id 主键ID
     * @return 库存变动日志
     */
    StockChangeLogDTO findById(Integer id);
    
    /**
     * 逻辑删除库存变动日志
     * @param id 主键ID
     * @return 操作结果
     */
    String deleteById(Integer id);

    /**
     * 分页查询库存变动日志
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByPage(int page, int pageSize);

    /**
     * 根据商品编码模糊查询分页
     * @param goodsCode 商品编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize);

    /**
     * 根据操作类型模糊查询分页
     * @param operationType 操作类型
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByOperationTypePage(String operationType, int page, int pageSize);

    /**
     * 根据仓库编码模糊查询分页
     * @param warehouseCode 仓库编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize);

    /**
     * 根据用户名称模糊查询分页
     * @param userName 用户名称
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByUserNamePage(String userName, int page, int pageSize);

    /**
     * 根据账号模糊查询分页
     * @param account 账号
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByAccountPage(String account, int page, int pageSize);

    /**
     * 根据出入库详情编码模糊查询分页
     * @param inOutDetailCode 出入库详情编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByInOutDetailCodePage(String inOutDetailCode, int page, int pageSize);
}

