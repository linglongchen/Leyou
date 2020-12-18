package com.modules.api.comon.weixin.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TokenQuery {
  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("jscode")
  private String jsCode;

  @ApiModelProperty("昵称")
  private String nickName;

  @ApiModelProperty("头像")
  private String avatarUrl;

  @ApiModelProperty()
  private String refreshToken;
}
