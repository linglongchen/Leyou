package com.modules.api.comon.weixin.entity;

import lombok.Data;

@Data
public class TokenConstant {
  private String access_token;
  private long expires_in;
}
