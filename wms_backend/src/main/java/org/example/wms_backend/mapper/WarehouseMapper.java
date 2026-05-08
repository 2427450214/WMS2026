package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.Warehouse;
import org.example.wms_backend.entity.WarehouseStockStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库表Mapper接口
 */
@Mapper
public interface WarehouseMapper {
    // 基本CRUD方法
    int insert(Warehouse warehouse);
    int updateByWarehouseCode(Warehouse warehouse);
    Warehouse selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);
    
    // 查询所有仓库
    List<Warehouse> selectAll();
    
    // 根据仓库状态查询仓库
    List<Warehouse> selectByStatus(@Param("status") String status);
    
    // 根据仓库类型查询仓库
    List<Warehouse> selectByWarehouseType(@Param("warehouseType") String warehouseType);
    
    // 根据仓库名称模糊查询仓库
    List<Warehouse> selectByWarehouseNameLike(@Param("warehouseName") String warehouseName);
    
    // 逻辑删除功能
    int updateIsDeletedByWarehouseCode(@Param("warehouseCode") String warehouseCode, @Param("isDeleted") Integer isDeleted);

    // 按仓库统计库存
    List<WarehouseStockStatistic> selectWarehouseStockStatistics();

    // 分页查询仓库
    List<Warehouse> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询仓库总数
    int selectCount();

    // 根据仓库名称模糊查询分页
    List<Warehouse> selectByWarehouseNameLikePage(@Param("warehouseName") String warehouseName, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据仓库名称模糊查询总数
    int selectCountByWarehouseNameLike(@Param("warehouseName") String warehouseName);

    // 根据仓库编码分页查询
    List<Warehouse> selectByWarehouseCodePage(@Param("warehouseCode") String warehouseCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据仓库编码查询总数
    int selectCountByWarehouseCode(@Param("warehouseCode") String warehouseCode);
}
