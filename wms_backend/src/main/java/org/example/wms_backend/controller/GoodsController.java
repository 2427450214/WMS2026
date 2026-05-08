package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.GoodsDTO;
import org.example.wms_backend.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 货物表控制器
 */
@RestController
@RequestMapping("/goods")
@Validated
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 新增商品
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addGoods(@Validated GoodsDTO goodsDTO) {
        // 调用服务层添加商品
        String result = goodsService.save(goodsDTO);
        return ResponseResult.success(result);
    }

    /**
     * 根据商品名模糊查询
     */
    @GetMapping("/findByName")
    public Map<String, Object> findByGoodsName(@RequestParam String goodsName) {
        // 检查商品名称
        if (goodsName == null || goodsName.trim().length() == 0) {
            return ResponseResult.paramError("商品名称不能为空");
        }
        if (goodsName.length() > 20) {
            return ResponseResult.paramError("商品名称长度不能超过20个字符");
        }

        // 调用服务层查询商品
        List<GoodsDTO> goodsDTOList = goodsService.findByGoodsName(goodsName);
        if (goodsDTOList != null && !goodsDTOList.isEmpty()) {
            return ResponseResult.success(goodsDTOList);
        } else {
            return ResponseResult.error("未找到对应的商品");
        }
    }

    /**
     * 查询所有商品
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<GoodsDTO> goodsDTOList = goodsService.findAll();
        if (goodsDTOList != null && !goodsDTOList.isEmpty()) {
            return ResponseResult.success(goodsDTOList);
        } else {
            return ResponseResult.error("未找到对应的商品");
        }
    }

    /**
     * 根据商品编码查询商品
     */
    @GetMapping("/findByCode")
    public Map<String, Object> findByGoodsCode(@RequestParam String goodsCode) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编码不能为空");
        }
        GoodsDTO goodsDTO = goodsService.findByGoodsCode(goodsCode);
        if (goodsDTO != null) {
            return ResponseResult.success(goodsDTO);
        } else {
            return ResponseResult.error("未找到对应的商品");
        }
    }

    /**
     * 根据分类查询商品
     */
    @GetMapping("/findByCategory")
    public Map<String, Object> findByCategory(@RequestParam(required = false) Integer categoryId) {
        if (categoryId == null) {
            return ResponseResult.error("分类ID不能为空");
        }
        List<GoodsDTO> goodsDTOList = goodsService.findByCategoryId(categoryId);
        if (goodsDTOList != null && !goodsDTOList.isEmpty()) {
            return ResponseResult.success(goodsDTOList);
        } else {
            return ResponseResult.error("未找到对应的商品");
        }
    }

    /**
     * 修改商品
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateGoods(@Validated GoodsDTO goodsDTO) {
        // 检查商品编码
        if (goodsDTO.getGoodsCode() == null) {
            return ResponseResult.paramError("商品编码不能为空");
        }

        // 调用服务层修改商品信息
        String result = goodsService.updateGoodsInfo(goodsDTO);
        return ResponseResult.success(result);
    }

    /**
     * 修改商品状态
     */
    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateStatus(@RequestParam String goodsCode, @RequestParam Integer isDeleted) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编码不能为空");
        }
        String result = goodsService.updateIsDeleted(goodsCode, isDeleted);
        return ResponseResult.success(result);
    }

    /**
     * 根据库存数量排序查询商品
     */
    @GetMapping("/orderByStock")
    public Map<String, Object> orderByStock(@RequestParam boolean asc) {
        List<GoodsDTO> goodsDTOList = goodsService.findOrderByStockQuantity(asc);
        return ResponseResult.success(goodsDTOList);
    }

    /**
     * 查询库存量接近最低库存预警线的商品
     */
    @GetMapping("/nearMinStock")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> nearMinStock() {
        List<GoodsDTO> goodsDTOList = goodsService.findNearMinStockWarning();
        return ResponseResult.success(goodsDTOList);
    }

    /**
     * 查询库存接近最高库存线的商品
     */
    @GetMapping("/nearMaxStock")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> nearMaxStock() {
        List<GoodsDTO> goodsDTOList = goodsService.findNearMaxStockWarning();
        return ResponseResult.success(goodsDTOList);
    }

    /**
     * 根据商品名查找商品编码
     */
    @GetMapping("/findCodeByName")
    public Map<String, Object> findCodeByGoodsName(@RequestParam String goodsName) {
        List<String> goodsCodeList = goodsService.findCodeByGoodsName(goodsName);
        if (goodsCodeList != null && !goodsCodeList.isEmpty()) {
            return ResponseResult.success(goodsCodeList);
        } else {
            return ResponseResult.error("未找到对应的商品编码");
        }
    }

    /**
     * 查询库存数量最多的前N个商品
     */
    @GetMapping("/getTopStockGoods")
    public Map<String, Object> getTopStockGoods(@RequestParam(defaultValue = "3") int limit) {
        List<org.example.wms_backend.dto.GoodsDTO> list = goodsService.findTopStockGoods(limit);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("暂无商品数据");
        }
    }

    /**
     * 分页查询商品
     */
    @GetMapping("/page")
    public Map<String, Object> getGoodsByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<GoodsDTO> pageResponse = goodsService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 按商品名模糊查询分页
     */
    @GetMapping("/searchPage")
    public Map<String, Object> searchGoodsByPage(@RequestParam String goodsName, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<GoodsDTO> pageResponse = goodsService.findByGoodsNamePage(goodsName, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 按分类查询分页
     */
    @GetMapping("/categoryPage")
    public Map<String, Object> getGoodsByCategoryPage(@RequestParam Integer categoryId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<GoodsDTO> pageResponse = goodsService.findByCategoryIdPage(categoryId, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}