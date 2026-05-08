package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类表Mapper接口
 */
@Mapper
public interface GoodsCategoryMapper {
    // 基本CRUD方法
    int insert(GoodsCategory goodsCategory);
    int updateByPrimaryKey(GoodsCategory goodsCategory);
    GoodsCategory selectByPrimaryKey(Integer id);
    
    // 查询所有商品分类
    List<GoodsCategory> selectAll();
    
    // 根据商品分类名称查询
    List<GoodsCategory> selectByCategoryName(@Param("categoryName") String categoryName);
    
    // 逻辑删除功能
    int updateIsDeleted(@Param("id") Integer id, @Param("isDeleted") Integer isDeleted);

    // 分页查询商品分类
    List<GoodsCategory> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询商品分类总数
    int selectCount();

    // 按分类名模糊查询分页
    List<GoodsCategory> selectByCategoryNamePage(@Param("categoryName") String categoryName, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 按分类名模糊查询总数
    int selectCountByCategoryName(@Param("categoryName") String categoryName);
}
