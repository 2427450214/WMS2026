package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.StockChangeLogDTO;
import org.example.wms_backend.service.StockChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库存变动日志表控制器
 */
@RestController
@RequestMapping("/stockChangeLog")
public class StockChangeLogController {

    @Autowired
    private StockChangeLogService stockChangeLogService;

    /**
     * 查询所有库存变动日志
     */
    @GetMapping("/findAll")
    public Map<String, Object> findAll() {
        List<StockChangeLogDTO> list = stockChangeLogService.findAll();
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到库存变动日志");
        }
    }

    /**
     * 根据操作类型查询库存变动日志
     */
    @GetMapping("/findByOperationType")
    public Map<String, Object> findByOperationType(@RequestParam String operationType) {
        if (operationType == null || operationType.trim().isEmpty()) {
            return ResponseResult.error("操作类型不能为空");
        }
        List<StockChangeLogDTO> list = stockChangeLogService.findByOperationType(operationType);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 根据仓库编号查询库存变动日志
     */
    @GetMapping("/findByWarehouseCode")
    public Map<String, Object> findByWarehouseCode(@RequestParam String warehouseCode) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            return ResponseResult.error("仓库编号不能为空");
        }
        List<StockChangeLogDTO> list = stockChangeLogService.findByWarehouseCode(warehouseCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 根据用户名称查询库存变动日志
     */
    @GetMapping("/findByUserName")
    public Map<String, Object> findByUserName(@RequestParam String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            return ResponseResult.error("用户名称不能为空");
        }
        List<StockChangeLogDTO> list = stockChangeLogService.findByUserName(userName);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 根据商品编号查询库存变动日志
     */
    @GetMapping("/findByGoodsCode")
    public Map<String, Object> findByGoodsCode(@RequestParam String goodsCode) {
        if (goodsCode == null || goodsCode.trim().isEmpty()) {
            return ResponseResult.error("商品编号不能为空");
        }
        List<StockChangeLogDTO> list = stockChangeLogService.findByGoodsCode(goodsCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 根据账号查询库存变动日志
     */
    @GetMapping("/findByAccount")
    public Map<String, Object> findByAccount(@RequestParam String account) {
        if (account == null || account.trim().isEmpty()) {
            return ResponseResult.error("账号不能为空");
        }
        List<StockChangeLogDTO> list = stockChangeLogService.findByAccount(account);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 根据出入库详细表查询库存变动日志
     */
    @GetMapping("/findByInOutDetailCode")
    public Map<String, Object> findByInOutDetailCode(@RequestParam String inOutDetailCode) {
        if (inOutDetailCode == null || inOutDetailCode.trim().isEmpty()) {
            return ResponseResult.error("出入库详细表编码不能为空");
        }
        List<StockChangeLogDTO> list = stockChangeLogService.findByInOutDetailCode(inOutDetailCode);
        if (list != null && !list.isEmpty()) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 根据主键查询库存变动日志
     */
    @GetMapping("/findById")
    public Map<String, Object> findById(@RequestParam Integer id) {
        if (id == null) {
            return ResponseResult.error("ID不能为空");
        }
        StockChangeLogDTO stockChangeLogDTO = stockChangeLogService.findById(id);
        if (stockChangeLogDTO != null) {
            return ResponseResult.success(stockChangeLogDTO);
        } else {
            return ResponseResult.error("未找到对应的库存变动日志");
        }
    }

    /**
     * 逻辑删除库存变动日志
     */
    @DeleteMapping("/delete")
    public Map<String, Object> deleteById(@RequestParam Integer id) {
        if (id == null) {
            return ResponseResult.error("ID不能为空");
        }
        String result = stockChangeLogService.deleteById(id);
        return ResponseResult.success(result);
    }

    /**
     * 分页查询库存变动日志
     */
    @GetMapping("/page")
    public Map<String, Object> getStockChangeLogByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据商品编码模糊查询分页
     */
    @GetMapping("/page/goodsCode")
    public Map<String, Object> getStockChangeLogByGoodsCodePage(@RequestParam String goodsCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByGoodsCodePage(goodsCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据操作类型模糊查询分页
     */
    @GetMapping("/page/operationType")
    public Map<String, Object> getStockChangeLogByOperationTypePage(@RequestParam String operationType, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByOperationTypePage(operationType, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据仓库编码模糊查询分页
     */
    @GetMapping("/page/warehouseCode")
    public Map<String, Object> getStockChangeLogByWarehouseCodePage(@RequestParam String warehouseCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByWarehouseCodePage(warehouseCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据用户名称模糊查询分页
     */
    @GetMapping("/page/userName")
    public Map<String, Object> getStockChangeLogByUserNamePage(@RequestParam String userName, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByUserNamePage(userName, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据账号模糊查询分页
     */
    @GetMapping("/page/account")
    public Map<String, Object> getStockChangeLogByAccountPage(@RequestParam String account, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByAccountPage(account, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据出入库详情编码模糊查询分页
     */
    @GetMapping("/page/inOutDetailCode")
    public Map<String, Object> getStockChangeLogByInOutDetailCodePage(@RequestParam String inOutDetailCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> pageResponse = stockChangeLogService.findByInOutDetailCodePage(inOutDetailCode, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}
