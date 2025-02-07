package com.orjrs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
} 