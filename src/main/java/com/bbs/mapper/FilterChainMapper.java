package com.bbs.mapper;

import com.bbs.entity.database.FilterChainDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FilterChainMapper {

    @Select("SELECT id,path,NAME FROM user_filter_chain ORDER BY id DESC")
    List<FilterChainDO> listFilterChain();
}
