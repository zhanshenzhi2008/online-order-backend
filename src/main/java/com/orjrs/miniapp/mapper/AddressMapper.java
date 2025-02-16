package com.orjrs.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.miniapp.entity.Address;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
} 