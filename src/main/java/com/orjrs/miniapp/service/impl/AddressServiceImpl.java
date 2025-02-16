package com.orjrs.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.common.exception.BusinessException;
import com.orjrs.miniapp.entity.Address;
import com.orjrs.miniapp.mapper.AddressMapper;
import com.orjrs.miniapp.service.AddressService;
import com.orjrs.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地址服务实现类
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getAddressList(String userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getUpdateTime);
        return list(wrapper);
    }

    @Override
    public Address getAddressDetail(String id) {
        Address address = getById(id);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        // 验证地址所属用户
        if (!address.getUserId().equals(UserContext.getCurrentUserId())) {
            throw new BusinessException("无权访问该地址");
        }
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address createAddress(Address address) {
        // 设置用户ID
        address.setUserId(UserContext.getCurrentUserId());
        // 设置时间
        LocalDateTime now = LocalDateTime.now();
        address.setCreateTime(now);
        address.setUpdateTime(now);
        
        // 如果是第一个地址，设为默认地址
        if (count(new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId, address.getUserId())) == 0) {
            address.setIsDefault(true);
        }
        
        // 如果设置为默认地址，需要将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            resetOtherDefaultAddress(address.getUserId());
        }
        
        // 保存地址
        save(address);
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address updateAddress(Address address) {
        // 验证地址是否存在
        Address existingAddress = getById(address.getId());
        if (existingAddress == null) {
            throw new BusinessException("地址不存在");
        }
        
        // 验证地址所属用户
        if (!existingAddress.getUserId().equals(UserContext.getCurrentUserId())) {
            throw new BusinessException("无权修改该地址");
        }
        
        // 如果设置为默认地址，需要将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            resetOtherDefaultAddress(existingAddress.getUserId());
        }
        
        // 更新时间
        address.setUpdateTime(LocalDateTime.now());
        
        // 更新地址
        updateById(address);
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAddress(String id) {
        // 验证地址是否存在
        Address address = getById(id);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        
        // 验证地址所属用户
        if (!address.getUserId().equals(UserContext.getCurrentUserId())) {
            throw new BusinessException("无权删除该地址");
        }
        
        // 删除地址
        boolean result = removeById(id);
        
        // 如果删除的是默认地址，且还有其他地址，则将第一个地址设为默认地址
        if (result && Boolean.TRUE.equals(address.getIsDefault())) {
            List<Address> remainingAddresses = getAddressList(address.getUserId());
            if (!remainingAddresses.isEmpty()) {
                Address firstAddress = remainingAddresses.get(0);
                firstAddress.setIsDefault(true);
                firstAddress.setUpdateTime(LocalDateTime.now());
                updateById(firstAddress);
            }
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address setDefaultAddress(String id) {
        // 验证地址是否存在
        Address address = getById(id);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        
        // 验证地址所属用户
        if (!address.getUserId().equals(UserContext.getCurrentUserId())) {
            throw new BusinessException("无权操作该地址");
        }
        
        // 将其他地址设为非默认
        resetOtherDefaultAddress(address.getUserId());
        
        // 设置为默认地址
        address.setIsDefault(true);
        address.setUpdateTime(LocalDateTime.now());
        updateById(address);
        
        return address;
    }

    @Override
    public Address getDefaultAddress(String userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, true);
        return getOne(wrapper);
    }

    /**
     * 重置用户的其他默认地址
     *
     * @param userId 用户ID
     */
    private void resetOtherDefaultAddress(String userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, true);
        
        Address defaultAddress = new Address();
        defaultAddress.setIsDefault(false);
        defaultAddress.setUpdateTime(LocalDateTime.now());
        
        update(defaultAddress, wrapper);
    }
} 