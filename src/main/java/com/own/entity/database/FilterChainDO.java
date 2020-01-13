package com.own.entity.database;

import lombok.Data;

/**
 * user_filter_chain表，shiro过滤链接和过滤器定义
 */
@Data
public class FilterChainDO {

    private int id;

    //过滤的链接
    private String path;

    //过滤器名称
    private String name;
}
