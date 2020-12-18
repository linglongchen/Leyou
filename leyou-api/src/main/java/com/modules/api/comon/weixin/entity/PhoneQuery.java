package com.modules.api.comon.weixin.entity;

import lombok.Data;

@Data
public class PhoneQuery {
  String encryptedData;
  String iv;
  String sessionKey;
}
