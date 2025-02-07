package com.orjrs.admin.service.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.goods.Goods;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

public interface GoodsService extends IService<Goods> {
    
    /**
     * 分页查询商品列表
     */
    IPage<Goods> page(int page, int size, Long categoryId, String name, Integer status);
    
    /**
     * 获取商品详情
     */
    Goods getDetail(Long id);
    
    /**
     * 保存商品
     */
    void saveGoods(Goods goods);
    
    /**
     * 更新商品
     */
    void updateGoods(Goods goods);
    
    /**
     * 删除商品
     */
    void deleteGoods(Long id);
    
    /**
     * 批量更新商品状态
     */
    void updateStatusBatch(Long[] ids, Integer status);
    
    /**
     * 更新商品库存
     */
    void updateStock(Long id, Integer stock);
    
    /**
     * 更新商品价格
     */
    void updatePrice(Long id, BigDecimal price);
    
    /**
     * 导入商品
     */
    void importGoods(MultipartFile file);
    
    /**
     * 导出商品
     */
    void exportGoods(Long categoryId, String name, Integer status, HttpServletResponse response);

    /**
     * 预占商品库存
     * @param id 商品ID
     * @param quantity 预占数量
     * @return 是否预占成功
     */
    boolean preoccupyStock(Long id, Integer quantity);

    /**
     * 释放商品库存
     * @param id 商品ID
     * @param quantity 释放数量
     */
    void releaseStock(Long id, Integer quantity);

    /**
     * 确认库存扣减
     * @param id 商品ID
     * @param quantity 确认数量
     */
    void confirmStock(Long id, Integer quantity);
} 