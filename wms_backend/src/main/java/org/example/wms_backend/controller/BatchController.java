package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.BatchDTO;
import org.example.wms_backend.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 批次表控制器
 */
@RestController
@RequestMapping("/batch")
@Validated
public class BatchController {

    @Autowired
    private BatchService batchService;

    /**
     * 新增批次
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addBatch(@Validated BatchDTO batchDTO) {
        String result = batchService.save(batchDTO);
        return ResponseResult.success(result);
    }

    /**
     * 查询所有批次
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<BatchDTO> batchDTOList = batchService.findAll();
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("未找到对应的批次");
        }
    }

    /**
     * 根据批次号查询批次
     */
    @GetMapping("/findByNumber")
    public Map<String, Object> findByBatchNumber(@RequestParam String batchNumber) {
        if (batchNumber == null || batchNumber.trim().isEmpty()) {
            return ResponseResult.error("批次号不能为空");
        }
        BatchDTO batchDTO = batchService.findByBatchNumber(batchNumber);
        if (batchDTO != null) {
            return ResponseResult.success(batchDTO);
        } else {
            return ResponseResult.error("未找到对应的批次");
        }
    }

    /**
     * 根据商品编码查询批次
     */
    @GetMapping("/findByGoodsCode")
    public Map<String, Object> findByGoodsCode(@RequestParam String goodsCode) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编码不能为空");
        }
        List<BatchDTO> batchDTOList = batchService.findByGoodsCode(goodsCode);
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("未找到对应的批次");
        }
    }

    /**
     * 根据仓库编码查询批次
     */
    @GetMapping("/findByWarehouseCode")
    public Map<String, Object> findByWarehouseCode(@RequestParam String warehouseCode) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编码不能为空");
        }
        List<BatchDTO> batchDTOList = batchService.findByWarehouseCode(warehouseCode);
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("未找到对应的批次");
        }
    }

    /**
     * 根据商品编码和仓库编码查询批次
     */
    @GetMapping("/findByGoodsAndWarehouse")
    public Map<String, Object> findByGoodsCodeAndWarehouseCode(@RequestParam String goodsCode, @RequestParam String warehouseCode) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编码不能为空");
        }
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编码不能为空");
        }
        List<BatchDTO> batchDTOList = batchService.findByGoodsCodeAndWarehouseCode(goodsCode, warehouseCode);
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("未找到对应的批次");
        }
    }

    /**
     * 根据状态查询批次
     */
    @GetMapping("/findByStatus")
    public Map<String, Object> findByStatus(@RequestParam String status) {
        if (status == null || status.trim().isEmpty()) {
            return ResponseResult.error("状态不能为空");
        }
        List<BatchDTO> batchDTOList = batchService.findByStatus(status);
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("未找到对应的批次");
        }
    }

    /**
     * 查询接近过期日期一个月的批次
     */
    @GetMapping("/nearExpiryOneMonth")
    public Map<String, Object> findNearExpiryBatchesOneMonth() {
        List<BatchDTO> batchDTOList = batchService.findNearExpiryBatchesOneMonth();
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("没有即将到期的批次");
        }
    }

    /**
     * 查询已过期的批次
     */
    @GetMapping("/getExpiredBatches")
    public Map<String, Object> getExpiredBatches() {
        List<BatchDTO> batchDTOList = batchService.findExpiredBatches();
        if (batchDTOList != null && !batchDTOList.isEmpty()) {
            return ResponseResult.success(batchDTOList);
        } else {
            return ResponseResult.error("暂无已过期的批次");
        }
    }

    /**
     * 分页查询批次
     */
    @GetMapping("/page")
    public Map<String, Object> getBatchByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据批次号模糊查询分页
     */
    @GetMapping("/page/batchNo")
    public Map<String, Object> getBatchByBatchNoPage(@RequestParam String batchNo, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findByBatchNoPage(batchNo, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据仓库编码分页查询
     */
    @GetMapping("/page/warehouseCode")
    public Map<String, Object> getBatchByWarehouseCodePage(@RequestParam String warehouseCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findByWarehouseCodePage(warehouseCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据商品编码分页查询
     */
    @GetMapping("/page/goodsCode")
    public Map<String, Object> getBatchByGoodsCodePage(@RequestParam String goodsCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findByGoodsCodePage(goodsCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据状态分页查询
     */
    @GetMapping("/page/status")
    public Map<String, Object> getBatchByStatusPage(@RequestParam String status, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findByStatusPage(status, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 分页查询接近过期一个月的批次
     */
    @GetMapping("/page/nearExpiryOneMonth")
    public Map<String, Object> getNearExpiryBatchesOneMonthPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findNearExpiryBatchesOneMonthPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 分页查询已过期的批次
     */
    @GetMapping("/page/expired")
    public Map<String, Object> getExpiredBatchesPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<BatchDTO> pageResponse = batchService.findExpiredBatchesPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}
