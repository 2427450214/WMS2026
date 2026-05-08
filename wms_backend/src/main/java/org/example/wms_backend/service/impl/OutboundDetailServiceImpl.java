package org.example.wms_backend.service.impl;

import org.example.wms_backend.common.StringUtil;
import org.example.wms_backend.dto.BatchDTO;
import org.example.wms_backend.dto.OutboundDetailDTO;
import org.example.wms_backend.entity.Batch;
import org.example.wms_backend.entity.Goods;
import org.example.wms_backend.entity.OutboundDetail;
import org.example.wms_backend.entity.Warehouse;
import org.example.wms_backend.mapper.BatchMapper;
import org.example.wms_backend.mapper.GoodsMapper;
import org.example.wms_backend.mapper.OutboundDetailMapper;
import org.example.wms_backend.mapper.StockChangeLogMapper;
import org.example.wms_backend.mapper.WarehouseMapper;
import org.example.wms_backend.entity.StockChangeLog;
import org.springframework.data.redis.core.RedisTemplate;
import org.example.wms_backend.service.BatchService;
import org.example.wms_backend.service.OutboundDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 出库详情表服务实现类
 */
@Service
public class OutboundDetailServiceImpl implements OutboundDetailService {

    @Autowired
    private OutboundDetailMapper outboundDetailMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private BatchMapper batchMapper;

    @Autowired
    private BatchService batchService;

    @Autowired
    private StockChangeLogMapper stockChangeLogMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private org.example.wms_backend.mapper.UserMapper userMapper;

    @Override
    @Transactional
    public String save(OutboundDetailDTO outboundDetailDTO) {
        // 从Spring Security中获取当前登录用户信息
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.example.wms_backend.exception.BusinessException("当前未登录，请登录");
        }
        
        // 获取当前登录用户名（账号）
        String currentAccount = authentication.getName();
        
        // 如果是匿名用户，返回错误
        if ("anonymousUser".equals(currentAccount)) {
            throw new org.example.wms_backend.exception.BusinessException("当前未登录，请登录");
        }
        
        // 从数据库获取用户信息
        org.example.wms_backend.entity.User user = userMapper.selectByAccount(currentAccount);
        if (user == null) {
            throw new org.example.wms_backend.exception.BusinessException("当前未登录，请登录");
        }
        
        // 检查仓库编码是否存在
        Warehouse warehouse = warehouseMapper.selectByWarehouseCode(outboundDetailDTO.getWarehouseCode());
        if (warehouse == null) {
            throw new org.example.wms_backend.exception.BusinessException("新增出库明细失败：仓库编码不存在");
        }

        // 检查商品编码是否存在
        Goods goods = goodsMapper.selectByGoodsCode(outboundDetailDTO.getGoodsCode());
        if (goods == null) {
            throw new org.example.wms_backend.exception.BusinessException("新增出库明细失败：商品编码不存在");
        }

        // 检查批次号是否存在
        Batch batch = batchMapper.selectByBatchCode(outboundDetailDTO.getBatchNo());
        if (batch == null) {
            throw new org.example.wms_backend.exception.BusinessException("新增出库明细失败：批次号不存在");
        }

        // 检查批次号与商品编码和仓库编码是否对应
        if (!batch.getGoodsCode().equals(outboundDetailDTO.getGoodsCode())) {
            throw new org.example.wms_backend.exception.BusinessException("新增出库明细失败：批次号与商品编码不对应");
        }
        if (!batch.getWarehouseCode().equals(outboundDetailDTO.getWarehouseCode())) {
            throw new org.example.wms_backend.exception.BusinessException("新增出库明细失败：批次号与仓库编码不对应");
        }

        // 检查出库数量是否大于该批次号的剩余数量
        if (outboundDetailDTO.getOutboundQuantity() > batch.getRemainingQuantity()) {
            throw new org.example.wms_backend.exception.BusinessException("新增出库明细失败：出库数量大于批次剩余数量");
        }

        // 生成唯一的出库单ID
        String outboundId;
        do {
            outboundId = "CK" + StringUtil.generateRandomString(8);
        } while (outboundDetailMapper.selectByOutboundId(outboundId).size() > 0);

        // 更新批次的剩余数量
        batch.setRemainingQuantity(batch.getRemainingQuantity() - outboundDetailDTO.getOutboundQuantity());
        batch.setUpdateTime(new Date());
        batchMapper.updateByPrimaryKey(batch);

        // 更新商品库存数量
        goodsMapper.updateStockQuantity(outboundDetailDTO.getGoodsCode(), -outboundDetailDTO.getOutboundQuantity());

        // 创建出库明细记录
        OutboundDetail outboundDetail = new OutboundDetail();
        BeanUtils.copyProperties(outboundDetailDTO, outboundDetail);
        outboundDetail.setOutboundId(outboundId);
        outboundDetail.setCreateTime(new Date());
        outboundDetail.setUpdateTime(new Date());
        outboundDetail.setIsDeleted(0);

        // 保存出库明细记录
        outboundDetailMapper.insert(outboundDetail);

        // 检查到期日期接近的批次
        List<BatchDTO> nearExpiryBatches = batchService.findNearExpiryBatches();
        String resultMessage = "新增出库明细成功，出库单ID：" + outboundId;
        if (nearExpiryBatches != null && !nearExpiryBatches.isEmpty()) {
            resultMessage += "（提示：存在" + nearExpiryBatches.size() + "个批次即将到期，请及时处理）";
        }

        // 创建库存变动日志
        StockChangeLog stockChangeLog = new StockChangeLog();
        stockChangeLog.setGoodsCode(outboundDetailDTO.getGoodsCode());
        stockChangeLog.setWarehouseCode(outboundDetailDTO.getWarehouseCode());
        stockChangeLog.setBeforeQuantity(goods.getStockQuantity()); // 变动前数量
        stockChangeLog.setChangeQuantity(-outboundDetailDTO.getOutboundQuantity()); // 变动数量（负数表示减少）
        stockChangeLog.setAfterQuantity(goods.getStockQuantity() - outboundDetailDTO.getOutboundQuantity()); // 变动后数量
        stockChangeLog.setOperationType("出库");
        stockChangeLog.setInOutDetailCode(outboundId);
        
        // 使用从数据库获取的用户信息
        stockChangeLog.setAccount(user.getAccount());
        stockChangeLog.setUserName(user.getName());
        
        stockChangeLog.setOperationTime(new Date()); // 操作时间等于创建时间
        stockChangeLog.setCreateTime(new Date());
        stockChangeLog.setUpdateTime(new Date());
        stockChangeLog.setIsDeleted(0);
        
        // 保存库存变动日志
        stockChangeLogMapper.insert(stockChangeLog);

        return resultMessage;
    }

    @Override
    public String deleteByOutboundId(String outboundId) {
        // 检查出库单是否存在
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByOutboundId(outboundId);
        if (outboundDetails == null || outboundDetails.isEmpty()) {
            throw new org.example.wms_backend.exception.BusinessException("删除失败：出库单ID不存在");
        }

        // 撤回库存数量和批次剩余数量
        for (OutboundDetail outboundDetail : outboundDetails) {
            // 增加商品库存数量
            goodsMapper.updateStockQuantity(outboundDetail.getGoodsCode(), outboundDetail.getOutboundQuantity());
            
            // 增加批次剩余数量
            Batch batch = batchMapper.selectByBatchCode(outboundDetail.getBatchNo());
            if (batch != null) {
                batch.setRemainingQuantity(batch.getRemainingQuantity() + outboundDetail.getOutboundQuantity());
                batch.setUpdateTime(new Date());
                batchMapper.updateByPrimaryKey(batch);
            }
        }

        // 逻辑删除对应的库存变动日志
        stockChangeLogMapper.updateIsDeletedByInOutDetailCode(outboundId);
        
        // 执行逻辑删除
        outboundDetailMapper.updateIsDeletedByOutboundId(outboundId, 1);
        return "删除出库明细成功，库存和批次剩余数量已撤回，对应日志已同步删除";
    }

    @Override
    public List<OutboundDetailDTO> findAll() {
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectAll();
        return convertToDTOList(outboundDetails);
    }

    @Override
    public List<OutboundDetailDTO> findByGoodsCode(String goodsCode) {
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByGoodsCode(goodsCode);
        return convertToDTOList(outboundDetails);
    }

    @Override
    public List<OutboundDetailDTO> findByWarehouseCode(String warehouseCode) {
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByWarehouseCode(warehouseCode);
        return convertToDTOList(outboundDetails);
    }

    @Override
    public List<OutboundDetailDTO> findByBatchNo(String batchNo) {
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByBatchNo(batchNo);
        return convertToDTOList(outboundDetails);
    }

    @Override
    public List<OutboundDetailDTO> findByOutboundId(String outboundId) {
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByOutboundId(outboundId);
        return convertToDTOList(outboundDetails);
    }

    /**
     * 将OutboundDetail实体列表转换为OutboundDetailDTO列表
     * @param outboundDetails OutboundDetail实体列表
     * @return OutboundDetailDTO列表
     */
    private List<OutboundDetailDTO> convertToDTOList(List<OutboundDetail> outboundDetails) {
        List<OutboundDetailDTO> dtoList = new ArrayList<>();
        if (outboundDetails != null && !outboundDetails.isEmpty()) {
            for (OutboundDetail outboundDetail : outboundDetails) {
                OutboundDetailDTO dto = new OutboundDetailDTO();
                BeanUtils.copyProperties(outboundDetail, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<org.example.wms_backend.entity.GoodsOutboundStatistic> getWeeklyOutboundStatistic() {
        return outboundDetailMapper.selectWeeklyOutboundStatistic();
    }

    @Override
    public List<org.example.wms_backend.entity.DailyOutboundStatistic> getDailyOutboundStatistic() {
        return outboundDetailMapper.selectDailyOutboundStatistic();
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByPage(offset, pageSize);
        int total = outboundDetailMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(outboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByGoodsCodeLikePage(goodsCode, offset, pageSize);
        int total = outboundDetailMapper.selectCountByGoodsCodeLike(goodsCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(outboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByWarehouseCodePage(warehouseCode, offset, pageSize);
        int total = outboundDetailMapper.selectCountByWarehouseCode(warehouseCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(outboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByBatchNoPage(String batchNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByBatchNoPage(batchNo, offset, pageSize);
        int total = outboundDetailMapper.selectCountByBatchNo(batchNo);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(outboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByOutboundIdPage(String outboundId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByOutboundIdPage(outboundId, offset, pageSize);
        int total = outboundDetailMapper.selectCountByOutboundId(outboundId);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(outboundDetails), total, page, pageSize);
    }
}