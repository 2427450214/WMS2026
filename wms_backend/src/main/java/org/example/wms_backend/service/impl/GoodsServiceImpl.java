package org.example.wms_backend.service.impl;

import org.example.wms_backend.common.StringUtil;
import org.example.wms_backend.dto.GoodsDTO;
import org.example.wms_backend.entity.Goods;
import org.example.wms_backend.mapper.GoodsMapper;
import org.example.wms_backend.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 货物表服务实现类
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public String save(GoodsDTO goodsDTO) {
        // 自动生成商品编码
        String goodsCode;
        do {
            goodsCode = "SP" + StringUtil.generateRandomString(8);
        } while (goodsMapper.selectByGoodsCode(goodsCode) != null);
        
        // 将DTO转换为实体类
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);
        
        // 手动映射预警线字段
        if (goodsDTO.getMinStockAlert() != null) {
            goods.setMinStockWarning(goodsDTO.getMinStockAlert());
        }
        if (goodsDTO.getMaxStockAlert() != null) {
            goods.setMaxStockWarning(goodsDTO.getMaxStockAlert());
        }
        
        // 设置商品编码
        goods.setGoodsCode(goodsCode);

        // 设置默认值
        if (goods.getStockQuantity() == null) {
            goods.setStockQuantity(0);
        }
        if (goods.getMinStockWarning() == null) {
            goods.setMinStockWarning(0);
        }
        if (goods.getMaxStockWarning() == null) {
            goods.setMaxStockWarning(0);
        }
        
        Date now = new Date();
        goods.setCreateTime(now);
        goods.setUpdateTime(now);
        goods.setIsDeleted(0);

        // 保存数据
        goodsMapper.insert(goods);

        return "添加商品成功";
    }

    @Override
    public List<GoodsDTO> findByGoodsName(String goodsName) {
        // 使用MyBatis的方法实现模糊查询
        List<Goods> goodsList = goodsMapper.selectByGoodsNameLike(goodsName);
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsList);
    }
    
    @Override
    public List<GoodsDTO> findAll() {
        // 查询所有商品
        List<Goods> goodsList = goodsMapper.selectAll();
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsList);
    }
    
    @Override
    public GoodsDTO findByGoodsCode(String goodsCode) {
        // 根据商品编码查询商品
        Goods goods = goodsMapper.selectByGoodsCode(goodsCode);
        
        // 将实体类转换为DTO
        if (goods != null) {
            return convertToDTO(goods);
        }
        return null;
    }
    
    @Override
    public List<GoodsDTO> findByCategoryId(Integer categoryId) {
        // 根据分类查询商品
        List<Goods> goodsList = goodsMapper.selectByCategoryId(categoryId);
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsList);
    }
    
    @Override
    public String updateGoodsInfo(GoodsDTO goodsDTO) {
        // 检查商品是否存在
        if (goodsDTO.getGoodsCode() == null) {
            throw new org.example.wms_backend.exception.BusinessException("修改商品失败：商品编码不能为空");
        }
        
        Goods existingGoods = goodsMapper.selectByGoodsCode(goodsDTO.getGoodsCode());
        if (existingGoods == null) {
            throw new org.example.wms_backend.exception.BusinessException("修改商品失败：商品不存在");
        }
        
        // 将DTO转换为实体类
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);
        
        // 手动映射预警线字段
        if (goodsDTO.getMinStockAlert() != null) {
            goods.setMinStockWarning(goodsDTO.getMinStockAlert());
        }
        if (goodsDTO.getMaxStockAlert() != null) {
            goods.setMaxStockWarning(goodsDTO.getMaxStockAlert());
        }
        
        // 设置修改时间
        goods.setUpdateTime(new Date());
        
        // 更新商品信息
        goodsMapper.updateGoodsInfoByGoodsCode(goods);
        
        return "修改商品成功";
    }
    

    
    @Override
    public String updateIsDeleted(String goodsCode, Integer isDeleted) {
        // 检查商品是否存在
        Goods existingGoods = goodsMapper.selectByGoodsCode(goodsCode);
        if (existingGoods == null) {
            throw new org.example.wms_backend.exception.BusinessException("修改商品状态失败：商品不存在");
        }
        
        // 修改逻辑删除状态
        goodsMapper.updateIsDeletedByGoodsCode(goodsCode, isDeleted);
        
        return "修改商品状态成功";
    }
    
    @Override
    public List<GoodsDTO> findOrderByStockQuantity(boolean asc) {
        // 根据库存数量排序查询商品
        List<Goods> goodsList = goodsMapper.selectOrderByStockQuantity(asc);
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsList);
    }
    
    @Override
    public List<GoodsDTO> findNearMinStockWarning() {
        // 查询库存量接近最低库存预警线的商品
        List<Goods> goodsList = goodsMapper.selectNearMinStockWarning();
        
        // 将实体类转换为DTO
        return convertToDTOList(goodsList);
    }
    
    @Override
    public List<GoodsDTO> findNearMaxStockWarning() {
        // 查询库存接近最高库存线的商品
        List<Goods> goodsList = goodsMapper.selectNearMaxStockWarning();

        // 将实体类转换为DTO
        return convertToDTOList(goodsList);
    }

    @Override
    public List<String> findCodeByGoodsName(String goodsName) {
        List<Goods> goodsList = goodsMapper.selectByGoodsNameLike(goodsName);
        List<String> goodsCodeList = new ArrayList<>();
        if (goodsList != null && !goodsList.isEmpty()) {
            for (Goods goods : goodsList) {
                goodsCodeList.add(goods.getGoodsCode());
            }
        }
        return goodsCodeList;
    }
    
    @Override
    public boolean isNearMaxStock(String goodsCode) {
        // 根据商品编码查询商品
        Goods goods = goodsMapper.selectByGoodsCode(goodsCode);
        if (goods == null) {
            return false;
        }
        
        // 检查商品是否设置了最大库存量
        if (goods.getMaxStockWarning() == null || goods.getMaxStockWarning() <= 0) {
            return false;
        }
        
        // 检查库存数量是否达到最大库存量的90%
        return goods.getStockQuantity() >= goods.getMaxStockWarning() * 0.9;
    }

    @Override
    public List<org.example.wms_backend.dto.GoodsDTO> findTopStockGoods(int limit) {
        List<Goods> goodsList = goodsMapper.selectTopStockGoods(limit);
        return convertToDTOList(goodsList);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Goods> goodsList = goodsMapper.selectByPage(offset, pageSize);
        int total = goodsMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(goodsList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsDTO> findByGoodsNamePage(String goodsName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Goods> goodsList = goodsMapper.selectByGoodsNameLikePage(goodsName, offset, pageSize);
        int total = goodsMapper.selectCountByGoodsNameLike(goodsName);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(goodsList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.GoodsDTO> findByCategoryIdPage(Integer categoryId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Goods> goodsList = goodsMapper.selectByCategoryIdPage(categoryId, offset, pageSize);
        int total = goodsMapper.selectCountByCategoryId(categoryId);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(goodsList), total, page, pageSize);
    }
    
    /**
     * 将Goods实体类转换为GoodsDTO
     */
    private GoodsDTO convertToDTO(Goods goods) {
        GoodsDTO goodsDTO = new GoodsDTO();
        BeanUtils.copyProperties(goods, goodsDTO);
        
        // 注意：Goods中的minStockWarning和maxStockWarning需要映射到GoodsDTO的minStockAlert和maxStockAlert
        if (goods.getMinStockWarning() != null) {
            goodsDTO.setMinStockAlert(goods.getMinStockWarning());
        }
        if (goods.getMaxStockWarning() != null) {
            goodsDTO.setMaxStockAlert(goods.getMaxStockWarning());
        }
        
        return goodsDTO;
    }
    
    /**
     * 将Goods实体类列表转换为GoodsDTO列表
     */
    private List<GoodsDTO> convertToDTOList(List<Goods> goodsList) {
        List<GoodsDTO> goodsDTOList = new ArrayList<>();
        if (goodsList != null && !goodsList.isEmpty()) {
            for (Goods goods : goodsList) {
                goodsDTOList.add(convertToDTO(goods));
            }
        }
        return goodsDTOList;
    }
}
