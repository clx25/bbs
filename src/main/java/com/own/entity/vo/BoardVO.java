package com.own.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("版块数据")
public class BoardVO {
    @ApiModelProperty("版块id")
    private int boardId;

    @ApiModelProperty("版块名")
    private String boardName;

    @ApiModelProperty("版块描述")
    private String description;


}
