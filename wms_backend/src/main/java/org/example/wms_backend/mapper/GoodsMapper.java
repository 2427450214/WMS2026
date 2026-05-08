package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.Goods;
import org.example.wms_backend.entity.CategoryStockStatistic;
import org.example.wms_backend.entity.StockWarningStatistic;
import org.example.wms_backend.entity.StockTurnoverStatistic;
import org.example.wms_backend.entity.HotGoodsStatistic;
import org.example.wms_backend.entity.SlowMovingGoodsStatistic;
import org.example.wms_backend.entity.DailyStockTrendStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品表Mapper接口
 */
@Mapper
public interface GoodsMapper {
    // 基本CRUD方法
    int insert(Goods goods);
    int updateByPrimaryKey(Goods goods);
    Goods selectByGoodsCode(@Param("goodsCode") String goodsCode);
    
    // 查询整个商品表全部数据
    List<Goods> selectAll();
    
    // 根据商品名进行模糊查询
    List<Goods> selectByGoodsNameLike(@Param("goodsName") String goodsName);
    
    // 根据分类查询商品
    List<Goods> selectByCategoryId(@Param("categoryId") Integer categoryId);
    
    // 修改逻辑删除
    int updateIsDeletedByGoodsCode(@Param("goodsCode") String goodsCode, @Param("isDeleted") Integer isDeleted);
    
    // 根据库存数量排序
    List<Goods> selectOrderByStockQuantity(@Param("asc") boolean asc);
    
    // 查询库存量接近最低库存预警线+100的商品
    List<Goods> selectNearMinStockWarning();
    
    // 查询库存接近最高库存线-100的商品
    List<Goods> selectNearMaxStockWarning();
    
    // 修改商品信息（只能修改商品名称，分类，库存数量，保质期，最低库存预警线，最高库存预警线）
    int updateGoodsInfoByGoodsCode(Goods goods);
    
    // 更新商品库存数量
    int updateStockQuantity(@Param("goodsCode") String goodsCode, @Param("quantity") Integer quantity);

    // 查询库存数量最多的前N个商品
    List<Goods> selectTopStockGoods(@Param("limit") int limit);

    // 按分类统计库存
    List<CategoryStockStatistic> selectCategoryStockStatistics();

    // 库存预警统计
    List<StockWarningStatistic> selectStockWarningStatistics();

    // 库存周转率统计
    List<StockTurnoverStatistic> selectStockTurnoverStatistics(@Param("days") int days);

    // 热门商品统计（近30天入库最多的商品）
    List<HotGoodsStatistic> selectHotGoodsStatistics(@Param("limit") int limit);

    // 滞销商品统计（近30天无出库的商品）
    List<SlowMovingGoodsStatistic> selectSlowMovingGoodsStatistics();

    // 每日库存趋势统计
    List<DailyStockTrendStatistic> selectDailyStockTrendStatistics(@Param("days") int days);

    // 分页查询商品
    List<Goods> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询商品总数
    int selectCount();

    // 按商品名模糊查询分页
    List<Goods> selectByGoodsNameLikePage(@Param("goodsName") String goodsName, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 按商品名模糊查询总数
    int selectCountByGoodsNameLike(@Param("goodsName") String goodsName);

    // 按分类查询分页
    List<Goods> selectByCategoryIdPage(@Param("categoryId") Integer categoryId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 按分类查询总数
    int selectCountByCategoryId(@Param("categoryId") Integer categoryId);
}
