package com.orjrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.entity.Address;
import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService extends IService<Address> {
    /**
     * 获取用户的地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<Address> getAddressList(String userId);

    /**
     * 获取地址详情
     *
     * @param id 地址ID
     * @return 地址详情
     */
    Address getAddressDetail(String id);

    /**
     * 创建地址
     *
     * @param address 地址信息
     * @return 创建的地址
     */
    Address createAddress(Address address);

    /**
     * 更新地址
     *
     * @param address 地址信息
     * @return 更新后的地址
     */
    Address updateAddress(Address address);

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 是否删除成功
     */
    boolean deleteAddress(String id);

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 设置后的地址
     */
    Address setDefaultAddress(String id);

    /**
     * 获取用户的默认地址
     *
     * @param userId 用户ID
     * @return 默认地址
     */
    Address getDefaultAddress(String userId);
} 