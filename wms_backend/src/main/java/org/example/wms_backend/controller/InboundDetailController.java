package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.InboundDetailDTO;
import org.example.wms_backend.service.InboundDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 入库明细控制器
 */
@RestController
@RequestMapping("/inboundDetail")
@Validated
public class InboundDetailController {

    @Autowired
    private InboundDetailService inboundDetailService;

    /**
     * 新增入库明细
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addInboundDetail(@Validated InboundDetailDTO inboundDetailDTO) {
        String result = inboundDetailService.save(inboundDetailDTO);
        return ResponseResult.success(result);
    }

    /**
     * 根据仓库编码查询入库明细
     */
    @GetMapping("/findByWarehouseCode")
    public Map<String, Object> findByWarehouseCode(@RequestParam String warehouseCode) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编码不能为空");
        }
        List<InboundDetailDTO> list = inboundDetailService.findByWarehouseCode(warehouseCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的入库明细");
        }
    }

    /**
     * 根据商品编码查询入库明细
     */
    @GetMapping("/findByGoodsCode")
    public Map<String, Object> findByGoodsCode(@RequestParam String goodsCode) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编码不能为空");
        }
        List<InboundDetailDTO> list = inboundDetailService.findByGoodsCode(goodsCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的入库明细");
        }
    }

    /**
     * 根据批次号查询入库明细
     */
    @GetMapping("/findByBatchNo")
    public Map<String, Object> findByBatchNo(@RequestParam String batchNo) {
        if (batchNo == null || batchNo.trim().isEmpty()) {
            return ResponseResult.error("批次号不能为空");
        }
        List<InboundDetailDTO> list = inboundDetailService.findByBatchNo(batchNo);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的入库明细");
        }
    }

    /**
     * 根据入库单ID查询入库明细
     */
    @GetMapping("/findByInboundId")
    public Map<String, Object> findByInboundId(@RequestParam String inboundId) {
        if (inboundId == null || inboundId.trim().isEmpty()) {
            return ResponseResult.error("入库单ID不能为空");
        }
        List<InboundDetailDTO> list = inboundDetailService.findByInboundId(inboundId);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的入库明细");
        }
    }

    /**
     * 根据入库单ID删除入库明细
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> deleteByInboundId(@RequestParam String inboundId) {
        if (inboundId == null || inboundId.trim().isEmpty()) {
            return ResponseResult.error("入库单ID不能为空");
        }
        String result = inboundDetailService.deleteByInboundId(inboundId);
        return ResponseResult.success(result);
    }
    
    /**
     * 查询所有入库明细
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<InboundDetailDTO> list = inboundDetailService.findAll();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到入库明细");
        }
    }

    /**
     * 统计近一周内每个商品入库总数量
     */
    @GetMapping("/getWeeklyInboundStatistic")
    public Map<String, Object> getWeeklyInboundStatistic() {
        List<org.example.wms_backend.entity.GoodsInboundStatistic> list = inboundDetailService.getWeeklyInboundStatistic();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("近一周内暂无入库数据");
        }
    }

    /**
     * 统计近7天每天入库总数量
     */
    @GetMapping("/getDailyInboundStatistic")
    public Map<String, Object> getDailyInboundStatistic() {
        List<org.example.wms_backend.entity.DailyInboundStatistic> list = inboundDetailService.getDailyInboundStatistic();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("近7天内暂无入库数据");
        }
    }

    /**
     * 分页查询入库明细
     */
    @GetMapping("/page")
    public Map<String, Object> getInboundDetailByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> pageResponse = inboundDetailService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据商品编码模糊查询分页
     */
    @GetMapping("/page/goodsCode")
    public Map<String, Object> getInboundDetailByGoodsCodePage(@RequestParam String goodsCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> pageResponse = inboundDetailService.findByGoodsCodePage(goodsCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据仓库编码分页查询
     */
    @GetMapping("/page/warehouseCode")
    public Map<String, Object> getInboundDetailByWarehouseCodePage(@RequestParam String warehouseCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> pageResponse = inboundDetailService.findByWarehouseCodePage(warehouseCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据批次号分页查询
     */
    @GetMapping("/page/batchNo")
    public Map<String, Object> getInboundDetailByBatchNoPage(@RequestParam String batchNo, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> pageResponse = inboundDetailService.findByBatchNoPage(batchNo, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据入库单ID分页查询
     */
    @GetMapping("/page/inboundId")
    public Map<String, Object> getInboundDetailByInboundIdPage(@RequestParam String inboundId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> pageResponse = inboundDetailService.findByInboundIdPage(inboundId, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}
