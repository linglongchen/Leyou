package com.leyou.upload.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @author Administrator
 *
 * 解决jmx重复注册bean的问题
 */
//@Configuration
//@Import(FdfsClientConfig.class)
//@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
//public class FastClientImporter {
//
//}
