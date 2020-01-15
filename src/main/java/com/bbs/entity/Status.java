package com.bbs.entity;

import com.bbs.util.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 传入所有页面的提示消息实体类
 */


@Data
@ApiModel("返回信息")
public class Status {
    @ApiModelProperty("状态码")
    private int code;
    @ApiModelProperty("状态描述")
    private String msg;

    public void setStatus(StatusEnum status) {
        this.code = status.code();
        this.msg = status.msg();
    }
}
