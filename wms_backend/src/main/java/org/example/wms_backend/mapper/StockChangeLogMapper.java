package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.StockChangeLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存变动日志表Mapper接口
 */
@Mapper
public interface StockChangeLogMapper {
    // 基本CRUD方法
    int insert(StockChangeLog stockChangeLog);
    int updateByPrimaryKey(StockChangeLog stockChangeLog);
    int deleteByPrimaryKey(Integer id);
    StockChangeLog selectByPrimaryKey(Integer id);
    
    // 根据操作类型查找
    List<StockChangeLog> selectByOperationType(@Param("operationType") String operationType);
    
    // 根据仓库编号查找
    List<StockChangeLog> selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);
    
    // 根据用户名称查找
    List<StockChangeLog> selectByUserName(@Param("userName") String userName);
    
    // 根据商品编号查找
    List<StockChangeLog> selectByGoodsCode(@Param("goodsCode") String goodsCode);
    
    // 根据账号查找
    List<StockChangeLog> selectByAccount(@Param("account") String account);
    
    // 根据出入库详细表查找
    List<StockChangeLog> selectByInOutDetailCode(@Param("inOutDetailCode") String inOutDetailCode);
    
    // 逻辑删除
    int updateIsDeletedById(@Param("id") Integer id);
    
    // 根据出入库详情编码逻辑删除
    int updateIsDeletedByInOutDetailCode(@Param("inOutDetailCode") String inOutDetailCode);
    
    // 查询所有
    List<StockChangeLog> selectAll();

    // 分页查询库存变动日志
    List<StockChangeLog> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询库存变动日志总数
    int selectCount();

    // 根据商品编码模糊查询分页
    List<StockChangeLog> selectByGoodsCodeLikePage(@Param("goodsCode") String goodsCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据商品编码模糊查询总数
    int selectCountByGoodsCodeLike(@Param("goodsCode") String goodsCode);

    // 根据操作类型模糊查询分页
    List<StockChangeLog> selectByOperationTypeLikePage(@Param("operationType") String operationType, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据操作类型模糊查询总数
    int selectCountByOperationTypeLike(@Param("operationType") String operationType);

    // 根据仓库编码模糊查询分页
    List<StockChangeLog> selectByWarehouseCodeLikePage(@Param("warehouseCode") String warehouseCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据仓库编码模糊查询总数
    int selectCountByWarehouseCodeLike(@Param("warehouseCode") String warehouseCode);

    // 根据用户名称模糊查询分页
    List<StockChangeLog> selectByUserNameLikePage(@Param("userName") String userName, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据用户名称模糊查询总数
    int selectCountByUserNameLike(@Param("userName") String userName);

    // 根据账号模糊查询分页
    List<StockChangeLog> selectByAccountLikePage(@Param("account") String account, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据账号模糊查询总数
    int selectCountByAccountLike(@Param("account") String account);

    // 根据出入库详情编码模糊查询分页
    List<StockChangeLog> selectByInOutDetailCodeLikePage(@Param("inOutDetailCode") String inOutDetailCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据出入库详情编码模糊查询总数
    int selectCountByInOutDetailCodeLike(@Param("inOutDetailCode") String inOutDetailCode);
}
