package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.OutboundDetailDTO;
import org.example.wms_backend.service.OutboundDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

/**
 * 出库详情表控制器
 */
@RestController
@RequestMapping("/outboundDetail")
@Validated
public class OutboundDetailController {

    @Autowired
    private OutboundDetailService outboundDetailService;

    /**
     * 新增出库明细
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addOutboundDetail(@Validated @RequestBody OutboundDetailDTO outboundDetailDTO) {
        String result = outboundDetailService.save(outboundDetailDTO);
        return ResponseResult.success(result);
    }
    
    /**
     * 根据出库单ID删除出库明细
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> deleteByOutboundId(@RequestParam String outboundId) {
        if (outboundId == null || outboundId.trim().isEmpty()) {
            return ResponseResult.error("出库单ID不能为空");
        }
        String result = outboundDetailService.deleteByOutboundId(outboundId);
        return ResponseResult.success(result);
    }
    
    /**
     * 查询所有出库明细
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<OutboundDetailDTO> list = outboundDetailService.findAll();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到出库明细");
        }
    }
    
    /**
     * 根据商品编码查询出库明细
     */
    @GetMapping("/findByGoodsCode")
    public Map<String, Object> findByGoodsCode(@RequestParam String goodsCode) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编码不能为空");
        }
        List<OutboundDetailDTO> list = outboundDetailService.findByGoodsCode(goodsCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的出库明细");
        }
    }
    
    /**
     * 根据仓库编码查询出库明细
     */
    @GetMapping("/findByWarehouseCode")
    public Map<String, Object> findByWarehouseCode(@RequestParam String warehouseCode) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编码不能为空");
        }
        List<OutboundDetailDTO> list = outboundDetailService.findByWarehouseCode(warehouseCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的出库明细");
        }
    }
    
    /**
     * 根据批次号查询出库明细
     */
    @GetMapping("/findByBatchNo")
    public Map<String, Object> findByBatchNo(@RequestParam String batchNo) {
        if (batchNo == null || batchNo.trim().isEmpty()) {
            return ResponseResult.error("批次号不能为空");
        }
        List<OutboundDetailDTO> list = outboundDetailService.findByBatchNo(batchNo);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的出库明细");
        }
    }
    
    /**
     * 根据出库单ID查询出库明细
     */
    @GetMapping("/findByOutboundId")
    public Map<String, Object> findByOutboundId(@RequestParam String outboundId) {
        if (outboundId == null || outboundId.trim().isEmpty()) {
            return ResponseResult.error("出库单ID不能为空");
        }
        List<OutboundDetailDTO> list = outboundDetailService.findByOutboundId(outboundId);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的出库明细");
        }
    }

    /**
     * 统计近一周内每个商品出库总数量
     */
    @GetMapping("/getWeeklyOutboundStatistic")
    public Map<String, Object> getWeeklyOutboundStatistic() {
        List<org.example.wms_backend.entity.GoodsOutboundStatistic> list = outboundDetailService.getWeeklyOutboundStatistic();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("近一周内暂无出库数据");
        }
    }

    /**
     * 统计近7天每天出库总数量
     */
    @GetMapping("/getDailyOutboundStatistic")
    public Map<String, Object> getDailyOutboundStatistic() {
        List<org.example.wms_backend.entity.DailyOutboundStatistic> list = outboundDetailService.getDailyOutboundStatistic();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("近7天内暂无出库数据");
        }
    }

    /**
     * 分页查询出库明细
     */
    @GetMapping("/page")
    public Map<String, Object> getOutboundDetailByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> pageResponse = outboundDetailService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据商品编码模糊查询分页
     */
    @GetMapping("/page/goodsCode")
    public Map<String, Object> getOutboundDetailByGoodsCodePage(@RequestParam String goodsCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> pageResponse = outboundDetailService.findByGoodsCodePage(goodsCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据仓库编码分页查询
     */
    @GetMapping("/page/warehouseCode")
    public Map<String, Object> getOutboundDetailByWarehouseCodePage(@RequestParam String warehouseCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> pageResponse = outboundDetailService.findByWarehouseCodePage(warehouseCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据批次号分页查询
     */
    @GetMapping("/page/batchNo")
    public Map<String, Object> getOutboundDetailByBatchNoPage(@RequestParam String batchNo, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> pageResponse = outboundDetailService.findByBatchNoPage(batchNo, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据出库单ID分页查询
     */
    @GetMapping("/page/outboundId")
    public Map<String, Object> getOutboundDetailByOutboundIdPage(@RequestParam String outboundId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> pageResponse = outboundDetailService.findByOutboundIdPage(outboundId, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}
