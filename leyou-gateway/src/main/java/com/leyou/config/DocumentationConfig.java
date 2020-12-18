package com.leyou.config;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

  @Autowired(required = false)
  private DiscoveryClient discoveryClient;

  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  public List<SwaggerResource> get() {
    List<SwaggerResource> resources = new ArrayList<>();
    resources.add(swaggerResource("spring-boot-provider", "/api/auth/v2/api-docs", "1.0.0"));
    return resources;
  }

  /**
   * 添加要聚合的子系统swagger资源对象
   * @param name 子系统文档的名称
   * @param location 聚合子系统swagger的文档api路径
   * @param version swagger的版本
   * @return
   */
  private SwaggerResource swaggerResource(String name, String location, String version) {
    SwaggerResource swaggerResource = new SwaggerResource();
    swaggerResource.setName(name);
    swaggerResource.setLocation(location);
    swaggerResource.setSwaggerVersion(version);
    return swaggerResource;
  }
}
