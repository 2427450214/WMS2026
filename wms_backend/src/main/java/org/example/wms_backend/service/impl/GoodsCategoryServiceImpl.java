package org.example.wms_backend.service.impl;

import org.example.wms_backend.dto.GoodsCategoryDTO;
import org.example.wms_backend.entity.GoodsCategory;
import org.example.wms_backend.mapper.GoodsCategoryMapper;
import org.example.wms_backend.service.GoodsCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 货物分类表服务实现类
 */
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public String save(GoodsCategoryDTO goodsCategoryDTO) {
        // 检查分类名称是否已存在
        List<GoodsCategory> existingCategories = goodsCategoryMapper.selectByCategoryName(goodsCategoryDTO.getCategoryName());
        if (existingCategories != null && !existingCategories.isEmpty()) {
            return "添加商品分类失败：分类名称已存在";
        }

        // 将DTO转换为实体类
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtils.copyProperties(goodsCategoryDTO, goodsCategory);

        Date now = new Date();
        goodsCategory.setCreateTime(now);
        goodsCategory.setUpdateTime(now);
        goodsCategory.setIsDeleted(0);

        // 保存数据
        goodsCategoryMapper.insert(goodsCategory);

        return "添加商品分类成功";
    }

    @Override
    public List<GoodsCategoryDTO> findAll() {
        // 查询所有商品分类
        List<GoodsCategory> goodsCategoryList = goodsCategoryMapper.selectAll();
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsCategoryList);
    }

    @Override
    public GoodsCategoryDTO findById(Integer id) {
        // 根据ID查询商品分类
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(id);
        
        // 将实体类转换为DTO
        if (goodsCategory != null) {
            return convertToDTO(goodsCategory);
        }
        return null;
    }

    @Override
    public List<GoodsCategoryDTO> findByCategoryName(String categoryName) {
        // 根据分类名称查询商品分类
        List<GoodsCategory> goodsCategoryList = goodsCategoryMapper.selectByCategoryName(categoryName);
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsCategoryList);
    }

    @Override
    public String updateGoodsCategory(GoodsCategoryDTO goodsCategoryDTO) {
        // 检查商品分类是否存在
        GoodsCategory existingCategory = goodsCategoryMapper.selectByPrimaryKey(goodsCategoryDTO.getId());
        if (existingCategory == null) {
            return "修改商品分类失败：商品分类不存在";
        }

        // 检查分类名称是否已存在（排除当前分类）
        List<GoodsCategory> existingCategories = goodsCategoryMapper.selectByCategoryName(goodsCategoryDTO.getCategoryName());
        if (existingCategories != null && !existingCategories.isEmpty()) {
            for (GoodsCategory category : existingCategories) {
                if (!category.getId().equals(goodsCategoryDTO.getId())) {
                    return "修改商品分类失败：分类名称已存在";
                }
            }
        }

        // 将DTO转换为实体类
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtils.copyProperties(goodsCategoryDTO, goodsCategory);

        // 更新数据
        goodsCategory.setUpdateTime(new Date());
        goodsCategoryMapper.updateByPrimaryKey(goodsCategory);

        return "修改商品分类成功";
    }

    @Override
    public String updateIsDeleted(Integer id, Integer isDeleted) {
        // 检查商品分类是否存在
        GoodsCategory existingCategory = goodsCategoryMapper.selectByPrimaryKey(id);
        if (existingCategory == null) {
            return "修改商品分类状态失败：商品分类不存在";
        }

        // 更新逻辑删除状态
        goodsCategoryMapper.updateIsDeleted(id, isDeleted);

        return "修改商品分类状态成功";
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<GoodsCategoryDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<GoodsCategory> categoryList = goodsCategoryMapper.selectByPage(offset, pageSize);
        int total = goodsCategoryMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(categoryList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<GoodsCategoryDTO> findByCategoryNamePage(String categoryName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<GoodsCategory> categoryList = goodsCategoryMapper.selectByCategoryNamePage(categoryName, offset, pageSize);
        int total = goodsCategoryMapper.selectCountByCategoryName(categoryName);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(categoryList), total, page, pageSize);
    }

    /**
     * 将GoodsCategory实体类转换为GoodsCategoryDTO
     */
    private GoodsCategoryDTO convertToDTO(GoodsCategory goodsCategory) {
        GoodsCategoryDTO goodsCategoryDTO = new GoodsCategoryDTO();
        BeanUtils.copyProperties(goodsCategory, goodsCategoryDTO);
        return goodsCategoryDTO;
    }

    /**
     * 将GoodsCategory实体类列表转换为GoodsCategoryDTO列表
     */
    private List<GoodsCategoryDTO> convertToDTOList(List<GoodsCategory> goodsCategoryList) {
        List<GoodsCategoryDTO> goodsCategoryDTOList = new ArrayList<>();
        if (goodsCategoryList != null && !goodsCategoryList.isEmpty()) {
            for (GoodsCategory goodsCategory : goodsCategoryList) {
                goodsCategoryDTOList.add(convertToDTO(goodsCategory));
            }
        }
        return goodsCategoryDTOList;
    }
}