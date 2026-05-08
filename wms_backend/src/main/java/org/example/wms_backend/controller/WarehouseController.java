package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.WarehouseDTO;
import org.example.wms_backend.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 仓库表控制器
 */
@RestController
@RequestMapping("/warehouse")
@Validated
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 新增仓库
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addWarehouse(@Validated WarehouseDTO warehouseDTO) {
        // 调用服务层添加仓库
        String result = warehouseService.save(warehouseDTO);
        return ResponseResult.success(result);
    }

    /**
     * 查询所有仓库
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<WarehouseDTO> warehouseDTOList = warehouseService.findAll();
        if (warehouseDTOList != null && !warehouseDTOList.isEmpty()) {
            return ResponseResult.success(warehouseDTOList);
        } else {
            return ResponseResult.error("未找到对应的仓库");
        }
    }

    /**
     * 根据仓库编码查询仓库
     */
    @GetMapping("/findByCode")
    public Map<String, Object> findByWarehouseCode(@RequestParam String warehouseCode) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编码不能为空");
        }
        WarehouseDTO warehouseDTO = warehouseService.findByWarehouseCode(warehouseCode);
        if (warehouseDTO != null) {
            return ResponseResult.success(warehouseDTO);
        } else {
            return ResponseResult.error("未找到对应的仓库");
        }
    }

    /**
     * 根据仓库状态查询仓库
     */
    @GetMapping("/findByStatus")
    public Map<String, Object> findByStatus(@RequestParam String status) {
        if (status == null || status.trim().isEmpty()) {
            return ResponseResult.error("仓库状态不能为空");
        }
        List<WarehouseDTO> warehouseDTOList = warehouseService.findByStatus(status);
        if (warehouseDTOList != null && !warehouseDTOList.isEmpty()) {
            return ResponseResult.success(warehouseDTOList);
        } else {
            return ResponseResult.error("未找到对应的仓库");
        }
    }

    /**
     * 根据仓库类型查询仓库
     */
    @GetMapping("/findByType")
    public Map<String, Object> findByWarehouseType(@RequestParam String warehouseType) {
        if (warehouseType == null || warehouseType.trim().isEmpty()) {
            return ResponseResult.error("仓库类型不能为空");
        }
        List<WarehouseDTO> warehouseDTOList = warehouseService.findByWarehouseType(warehouseType);
        if (warehouseDTOList != null && !warehouseDTOList.isEmpty()) {
            return ResponseResult.success(warehouseDTOList);
        } else {
            return ResponseResult.error("未找到对应的仓库");
        }
    }

    /**
     * 根据仓库名称模糊查询仓库
     */
    @GetMapping("/findByName")
    public Map<String, Object> findByWarehouseNameLike(@RequestParam String warehouseName) {
        if (warehouseName == null || warehouseName.trim().isEmpty()) {
            return ResponseResult.error("仓库名称不能为空");
        }
        List<WarehouseDTO> warehouseDTOList = warehouseService.findByWarehouseNameLike(warehouseName);
        if (warehouseDTOList != null && !warehouseDTOList.isEmpty()) {
            return ResponseResult.success(warehouseDTOList);
        } else {
            return ResponseResult.error("未找到对应的仓库");
        }
    }

    /**
     * 修改仓库
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateWarehouse(@Validated WarehouseDTO warehouseDTO) {
        // 检查仓库编码
        if (warehouseDTO.getWarehouseCode() == null) {
            return ResponseResult.paramError("仓库编码不能为空");
        }

        // 调用服务层修改仓库
        String result = warehouseService.updateWarehouse(warehouseDTO);
        return ResponseResult.success(result);
    }

    /**
     * 修改仓库状态
     */
    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateStatus(@RequestParam String warehouseCode, @RequestParam Integer isDeleted) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编码不能为空");
        }
        String result = warehouseService.updateIsDeleted(warehouseCode, isDeleted);
        return ResponseResult.success(result);
    }

    /**
     * 根据仓库名称查找仓库编码
     */
    @GetMapping("/findCodeByName")
    public Map<String, Object> findCodeByWarehouseName(@RequestParam String warehouseName) {
        if (warehouseName == null || warehouseName.trim().isEmpty()) {
            return ResponseResult.error("仓库名称不能为空");
        }
        List<String> warehouseCodeList = warehouseService.findCodeByWarehouseName(warehouseName);
        if (warehouseCodeList != null && !warehouseCodeList.isEmpty()) {
            return ResponseResult.success(warehouseCodeList);
        } else {
            return ResponseResult.error("未找到对应的仓库编码");
        }
    }

    /**
     * 分页查询仓库
     */
    @GetMapping("/page")
    public Map<String, Object> getWarehouseByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> pageResponse = warehouseService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据仓库名称模糊查询分页
     */
    @GetMapping("/page/name")
    public Map<String, Object> getWarehouseByWarehouseNamePage(@RequestParam String warehouseName, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> pageResponse = warehouseService.findByWarehouseNamePage(warehouseName, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据仓库编码模糊查询分页
     */
    @GetMapping("/page/code")
    public Map<String, Object> getWarehouseByWarehouseCodePage(@RequestParam String warehouseCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> pageResponse = warehouseService.findByWarehouseCodePage(warehouseCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}