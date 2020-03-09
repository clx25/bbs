package com.bbs.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 */
@Data
@ApiModel("发帖需要的数据")
public class AddThreadDTO {

    @NotEmpty(message = "标题不能为空")
    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("帖子内容")
    @NotEmpty(message = "帖子内容不能为空")
    @Size(max = 1000,message = "内容能大于1000个字符")
    private String content;

    @ApiModelProperty("版块id")
    @Min(value = 1,message = "boardId参数无效")
    private int boardId;

    //帖子id
    @JsonIgnore
    private int threadId;
    //用户id
    @JsonIgnore
    private int userId;


}
