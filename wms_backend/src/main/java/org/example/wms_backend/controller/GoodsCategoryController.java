package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.GoodsCategoryDTO;
import org.example.wms_backend.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品分类控制器
 */
@RestController
@RequestMapping("/goodsCategory")
@Validated
public class GoodsCategoryController {

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    /**
     * 添加商品分类
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addGoodsCategory(@Validated GoodsCategoryDTO goodsCategoryDTO) {
        // 调用服务层添加商品分类
        String result = goodsCategoryService.save(goodsCategoryDTO);
        return ResponseResult.success(result);
    }

    /**
     * 查询所有商品分类
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<GoodsCategoryDTO> goodsCategoryDTOList = goodsCategoryService.findAll();
        if (goodsCategoryDTOList != null && !goodsCategoryDTOList.isEmpty()) {
            return ResponseResult.success(goodsCategoryDTOList);
        } else {
            return ResponseResult.error("未找到对应的商品分类");
        }
    }

    /**
     * 根据ID查询商品分类
     */
    @GetMapping("/findById")
    public Map<String, Object> findById(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return ResponseResult.error("分类ID不能为空");
        }
        GoodsCategoryDTO goodsCategoryDTO = goodsCategoryService.findById(id);
        if (goodsCategoryDTO != null) {
            return ResponseResult.success(goodsCategoryDTO);
        } else {
            return ResponseResult.error("未找到对应的商品分类");
        }
    }

    /**
     * 根据名称查询商品分类
     */
    @GetMapping("/findByName")
    public Map<String, Object> findByCategoryName(@RequestParam String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return ResponseResult.error("分类名称不能为空");
        }
        List<GoodsCategoryDTO> goodsCategoryDTOList = goodsCategoryService.findByCategoryName(categoryName);
        if (goodsCategoryDTOList != null && !goodsCategoryDTOList.isEmpty()) {
            return ResponseResult.success(goodsCategoryDTOList);
        } else {
            return ResponseResult.error("未找到对应的商品分类");
        }
    }

    /**
     * 修改商品分类
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateGoodsCategory(@Validated GoodsCategoryDTO goodsCategoryDTO) {
        // 检查分类ID
        if (goodsCategoryDTO.getId() == null) {
            return ResponseResult.paramError("分类ID不能为空");
        }

        // 调用服务层修改商品分类
        String result = goodsCategoryService.updateGoodsCategory(goodsCategoryDTO);
        return ResponseResult.success(result);
    }

    /**
     * 修改商品分类状态
     */
    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateStatus(@RequestParam Integer id, @RequestParam Integer isDeleted) {
        if (id == null) {
            return ResponseResult.error("分类ID不能为空");
        }
        String result = goodsCategoryService.updateIsDeleted(id, isDeleted);
        return ResponseResult.success(result);
    }

    /**
     * 分页查询商品分类
     */
    @GetMapping("/page")
    public Map<String, Object> getCategoryByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<GoodsCategoryDTO> pageResponse = goodsCategoryService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 按分类名模糊查询分页
     */
    @GetMapping("/searchPage")
    public Map<String, Object> searchCategoryByPage(@RequestParam String categoryName, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<GoodsCategoryDTO> pageResponse = goodsCategoryService.findByCategoryNamePage(categoryName, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}