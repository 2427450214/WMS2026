package org.example.wms_backend.service.impl;

import org.example.wms_backend.common.StringUtil;
import org.example.wms_backend.dto.InboundDetailDTO;
import org.example.wms_backend.entity.Batch;
import org.example.wms_backend.entity.Goods;
import org.example.wms_backend.entity.InboundDetail;
import org.example.wms_backend.entity.OutboundDetail;
import org.example.wms_backend.entity.Warehouse;
import org.example.wms_backend.mapper.BatchMapper;
import org.example.wms_backend.mapper.GoodsMapper;
import org.example.wms_backend.mapper.InboundDetailMapper;
import org.example.wms_backend.mapper.OutboundDetailMapper;
import org.example.wms_backend.mapper.StockChangeLogMapper;
import org.example.wms_backend.mapper.WarehouseMapper;
import org.example.wms_backend.entity.StockChangeLog;
import org.springframework.data.redis.core.RedisTemplate;
import org.example.wms_backend.service.InboundDetailService;
import org.example.wms_backend.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 入库详情表服务实现类
 */
@Service
public class InboundDetailServiceImpl implements InboundDetailService {

    @Autowired
    private InboundDetailMapper inboundDetailMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private BatchMapper batchMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OutboundDetailMapper outboundDetailMapper;

    @Autowired
    private StockChangeLogMapper stockChangeLogMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private org.example.wms_backend.mapper.UserMapper userMapper;

    @Override
    @Transactional
    public String save(InboundDetailDTO inboundDetailDTO) {
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
        Warehouse warehouse = warehouseMapper.selectByWarehouseCode(inboundDetailDTO.getWarehouseCode());
        if (warehouse == null) {
            throw new org.example.wms_backend.exception.BusinessException("新增入库明细失败：仓库编码不存在");
        }

        // 检查商品编码是否存在
        Goods goods = goodsMapper.selectByGoodsCode(inboundDetailDTO.getGoodsCode());
        if (goods == null) {
            throw new org.example.wms_backend.exception.BusinessException("新增入库明细失败：商品编码不存在");
        }

        // 生成唯一的入库单ID
        String inboundId;
        do {
            inboundId = "RK" + StringUtil.generateRandomString(8);
        } while (inboundDetailMapper.selectByInboundId(inboundId).size() > 0);

        // 生成唯一的批次号
        String batchNo;
        do {
            batchNo = StringUtil.generateBatchNo();
        } while (batchMapper.selectByBatchCode(batchNo) != null);

        // 创建批次记录
        Batch batch = new Batch();
        batch.setBatchNo(batchNo);
        batch.setGoodsCode(inboundDetailDTO.getGoodsCode());
        batch.setProductionDate(new Date()); // 生产日期等于入库表单的创建日期
        
        // 计算到期日期
        if (goods.getShelfLife() != null && goods.getShelfLife() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, goods.getShelfLife());
            batch.setExpiryDate(calendar.getTime());
        }
        
        batch.setBatchQuantity(inboundDetailDTO.getInboundQuantity()); // 批次数量等同于入库数量
        batch.setRemainingQuantity(inboundDetailDTO.getInboundQuantity()); // 剩余数量初始等于批次数量
        batch.setWarehouseCode(inboundDetailDTO.getWarehouseCode());
        batch.setStatus("正常");
        batch.setCreateTime(new Date());
        batch.setUpdateTime(new Date());
        batch.setIsDeleted(0);
        
        // 保存批次记录
        batchMapper.insert(batch);

        // 创建入库明细记录
        InboundDetail inboundDetail = new InboundDetail();
        BeanUtils.copyProperties(inboundDetailDTO, inboundDetail);
        inboundDetail.setInboundId(inboundId);
        inboundDetail.setBatchNo(batchNo);
        inboundDetail.setCreateTime(new Date());
        inboundDetail.setUpdateTime(new Date());
        inboundDetail.setIsDeleted(0);

        // 保存入库明细记录
        inboundDetailMapper.insert(inboundDetail);

        // 更新商品库存数量
        goodsMapper.updateStockQuantity(inboundDetailDTO.getGoodsCode(), inboundDetailDTO.getInboundQuantity());

        // 检查商品是否接近最大库存量
        boolean isNearMaxStock = goodsService.isNearMaxStock(inboundDetailDTO.getGoodsCode());
        String resultMessage = "新增入库明细成功，批次号：" + batchNo;
        if (isNearMaxStock) {
            resultMessage += "（提示：该商品库存已接近最大库存量）";
        }

        // 创建库存变动日志
        StockChangeLog stockChangeLog = new StockChangeLog();
        stockChangeLog.setGoodsCode(inboundDetailDTO.getGoodsCode());
        stockChangeLog.setWarehouseCode(inboundDetailDTO.getWarehouseCode());
        stockChangeLog.setBeforeQuantity(goods.getStockQuantity()); // 变动前数量
        stockChangeLog.setChangeQuantity(inboundDetailDTO.getInboundQuantity()); // 变动数量
        stockChangeLog.setAfterQuantity(goods.getStockQuantity() + inboundDetailDTO.getInboundQuantity()); // 变动后数量
        stockChangeLog.setOperationType("入库");
        stockChangeLog.setInOutDetailCode(inboundId);
        
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
    public List<InboundDetailDTO> findByWarehouseCode(String warehouseCode) {
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByWarehouseCode(warehouseCode);
        return convertToDTOList(inboundDetails);
    }

    @Override
    public List<InboundDetailDTO> findByGoodsCode(String goodsCode) {
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByGoodsCode(goodsCode);
        return convertToDTOList(inboundDetails);
    }

    @Override
    public List<InboundDetailDTO> findByBatchNo(String batchNo) {
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByBatchNo(batchNo);
        return convertToDTOList(inboundDetails);
    }

    @Override
    public List<InboundDetailDTO> findByInboundId(String inboundId) {
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByInboundId(inboundId);
        return convertToDTOList(inboundDetails);
    }

    @Override
    public String deleteByInboundId(String inboundId) {
        // 检查入库单是否存在
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByInboundId(inboundId);
        if (inboundDetails == null || inboundDetails.isEmpty()) {
            throw new org.example.wms_backend.exception.BusinessException("删除失败：入库单ID不存在");
        }
        
        // 检查批次号是否有出库操作
        for (InboundDetail inboundDetail : inboundDetails) {
            List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByBatchNo(inboundDetail.getBatchNo());
            if (outboundDetails != null && !outboundDetails.isEmpty()) {
                throw new org.example.wms_backend.exception.BusinessException("删除失败：该入库表单对应的批次已存在出库操作，无法删除");
            }
        }
        
        // 撤回库存数量并逻辑删除对应的批次
        for (InboundDetail inboundDetail : inboundDetails) {
            // 从商品库存中减去入库数量
            goodsMapper.updateStockQuantity(inboundDetail.getGoodsCode(), -inboundDetail.getInboundQuantity());
            // 逻辑删除对应的批次
            batchMapper.updateIsDeletedByBatchCode(inboundDetail.getBatchNo());
        }
        
        // 逻辑删除对应的库存变动日志
        stockChangeLogMapper.updateIsDeletedByInOutDetailCode(inboundId);
        
        // 执行逻辑删除
        inboundDetailMapper.updateIsDeletedByInboundId(inboundId, 1);
        return "删除入库明细成功，库存已撤回，对应批次已同步撤回，对应日志已同步删除";
    }
    
    @Override
    public List<InboundDetailDTO> findAll() {
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectAll();
        return convertToDTOList(inboundDetails);
    }

    /**
     * 将InboundDetail实体列表转换为InboundDetailDTO列表
     * @param inboundDetails InboundDetail实体列表
     * @return InboundDetailDTO列表
     */
    private List<InboundDetailDTO> convertToDTOList(List<InboundDetail> inboundDetails) {
        List<InboundDetailDTO> dtoList = new ArrayList<>();
        if (inboundDetails != null && !inboundDetails.isEmpty()) {
            for (InboundDetail inboundDetail : inboundDetails) {
                InboundDetailDTO dto = new InboundDetailDTO();
                BeanUtils.copyProperties(inboundDetail, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<org.example.wms_backend.entity.GoodsInboundStatistic> getWeeklyInboundStatistic() {
        return inboundDetailMapper.selectWeeklyInboundStatistic();
    }

    @Override
    public List<org.example.wms_backend.entity.DailyInboundStatistic> getDailyInboundStatistic() {
        return inboundDetailMapper.selectDailyInboundStatistic();
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByPage(offset, pageSize);
        int total = inboundDetailMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(inboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByGoodsCodeLikePage(goodsCode, offset, pageSize);
        int total = inboundDetailMapper.selectCountByGoodsCodeLike(goodsCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(inboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByWarehouseCodePage(warehouseCode, offset, pageSize);
        int total = inboundDetailMapper.selectCountByWarehouseCode(warehouseCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(inboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByBatchNoPage(String batchNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByBatchNoPage(batchNo, offset, pageSize);
        int total = inboundDetailMapper.selectCountByBatchNo(batchNo);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(inboundDetails), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByInboundIdPage(String inboundId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByInboundIdPage(inboundId, offset, pageSize);
        int total = inboundDetailMapper.selectCountByInboundId(inboundId);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(inboundDetails), total, page, pageSize);
    }
}