package com.bbs.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("登录需要的数据")
public class SigninDTO extends UserBaseDTO {

    @ApiModelProperty("是否记住登录状态")
    private boolean remember;
}
