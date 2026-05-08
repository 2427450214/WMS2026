package org.example.wms_backend.service.impl;

import org.example.wms_backend.common.StringUtil;
import org.example.wms_backend.dto.BatchDTO;
import org.example.wms_backend.entity.Batch;
import org.example.wms_backend.entity.Goods;
import org.example.wms_backend.entity.Warehouse;
import org.example.wms_backend.mapper.BatchMapper;
import org.example.wms_backend.mapper.GoodsMapper;
import org.example.wms_backend.mapper.WarehouseMapper;
import org.example.wms_backend.service.BatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批次表服务实现类
 */
@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchMapper batchMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public String save(BatchDTO batchDTO) {
        // 检查商品编码是否存在
        Goods goods = goodsMapper.selectByGoodsCode(batchDTO.getGoodsCode());
        if (goods == null) {
            return "新增批次失败：商品编码不存在";
        }

        // 检查仓库编码是否存在
        Warehouse warehouse = warehouseMapper.selectByWarehouseCode(batchDTO.getWarehouseCode());
        if (warehouse == null) {
            return "新增批次失败：仓库编码不存在";
        }

        // 生成唯一的批次号
        String batchNo;
        do {
            batchNo = StringUtil.generateBatchNo();
        } while (batchMapper.selectByBatchCode(batchNo) != null);

        // 将DTO转换为实体类
        Batch batch = new Batch();
        BeanUtils.copyProperties(batchDTO, batch);
        batch.setBatchNo(batchNo);

        // 设置默认值
        Date now = new Date();
        batch.setCreateTime(now);
        batch.setUpdateTime(now);
        batch.setIsDeleted(0);

        // 批次数量初始化后不可修改，剩余数量与批次数量一致
        batch.setBatchQuantity(batchDTO.getBatchQuantity());
        batch.setRemainingQuantity(batchDTO.getBatchQuantity());

        // 状态初始为正常
        batch.setStatus("正常");

        // 保存数据
        batchMapper.insert(batch);

        return "新增批次成功";
    }

    @Override
    public List<BatchDTO> findAll() {
        List<Batch> batchList = batchMapper.selectAll();
        return convertToDTOList(batchList);
    }

    @Override
    public BatchDTO findByBatchNumber(String batchNumber) {
        Batch batch = batchMapper.selectByBatchCode(batchNumber);
        if (batch != null) {
            return convertToDTO(batch);
        }
        return null;
    }

    @Override
    public List<BatchDTO> findByGoodsCode(String goodsCode) {
        List<Batch> batchList = batchMapper.selectByGoodsCode(goodsCode);
        return convertToDTOList(batchList);
    }

    @Override
    public List<BatchDTO> findByWarehouseCode(String warehouseCode) {
        List<Batch> batchList = batchMapper.selectByWarehouseCode(warehouseCode);
        return convertToDTOList(batchList);
    }

    @Override
    public List<BatchDTO> findByGoodsCodeAndWarehouseCode(String goodsCode, String warehouseCode) {
        List<Batch> batchList = batchMapper.selectByGoodsCodeAndWarehouseCode(goodsCode, warehouseCode);
        return convertToDTOList(batchList);
    }

    @Override
    public List<BatchDTO> findByStatus(String status) {
        List<Batch> batches = batchMapper.selectByStatus(status);
        return convertToDTOList(batches);
    }
    
    @Override
    public List<BatchDTO> findNearExpiryBatches() {
        List<Batch> batches = batchMapper.selectNearExpiryBatches();
        return convertToDTOList(batches);
    }

    @Override
    public List<BatchDTO> findNearExpiryBatchesOneMonth() {
        List<Batch> batches = batchMapper.selectNearExpiryBatchesOneMonth();
        return convertToDTOList(batches);
    }

    @Override
    public int updateExpiredBatchStatus() {
        return batchMapper.updateExpiredBatchStatus();
    }

    @Override
    public List<org.example.wms_backend.dto.BatchDTO> findExpiredBatches() {
        List<Batch> batches = batchMapper.selectExpiredBatches();
        return convertToDTOList(batches);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectByPage(offset, pageSize);
        int total = batchMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByBatchNoPage(String batchNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectByBatchNoLikePage(batchNo, offset, pageSize);
        int total = batchMapper.selectCountByBatchNoLike(batchNo);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectByWarehouseCodePage(warehouseCode, offset, pageSize);
        int total = batchMapper.selectCountByWarehouseCode(warehouseCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectByGoodsCodePage(goodsCode, offset, pageSize);
        int total = batchMapper.selectCountByGoodsCode(goodsCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByStatusPage(String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectByStatusPage(status, offset, pageSize);
        int total = batchMapper.selectCountByStatus(status);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findNearExpiryBatchesOneMonthPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectNearExpiryBatchesOneMonthPage(offset, pageSize);
        int total = batchMapper.selectCountNearExpiryBatchesOneMonth();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findExpiredBatchesPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Batch> batches = batchMapper.selectExpiredBatchesPage(offset, pageSize);
        int total = batchMapper.selectCountExpiredBatches();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(batches), total, page, pageSize);
    }

    private BatchDTO convertToDTO(Batch batch) {
        BatchDTO batchDTO = new BatchDTO();
        BeanUtils.copyProperties(batch, batchDTO);
        batchDTO.setBatchNumber(batch.getBatchNo());
        
        // 添加商品名称
        Goods goods = goodsMapper.selectByGoodsCode(batch.getGoodsCode());
        if (goods != null) {
            batchDTO.setGoodsName(goods.getGoodsName());
        }
        
        // 添加仓库名称
        Warehouse warehouse = warehouseMapper.selectByWarehouseCode(batch.getWarehouseCode());
        if (warehouse != null) {
            batchDTO.setWarehouseName(warehouse.getWarehouseName());
        }
        
        return batchDTO;
    }

    private List<BatchDTO> convertToDTOList(List<Batch> batchList) {
        List<BatchDTO> batchDTOList = new ArrayList<>();
        if (batchList != null && !batchList.isEmpty()) {
            for (Batch batch : batchList) {
                batchDTOList.add(convertToDTO(batch));
            }
        }
        return batchDTOList;
    }
}