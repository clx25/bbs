package com.own.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Result<T> extends Status {

    @ApiModelProperty("数据")
    private T data;
}
