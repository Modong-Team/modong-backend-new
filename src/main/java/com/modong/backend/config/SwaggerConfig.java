package com.modong.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class SwaggerConfig {



  @Bean
  public GroupedOpenApi publicApi(){
    return GroupedOpenApi.builder()
        .group("modong-public")
        .pathsToMatch("/**")
        .pathsToExclude("/actuator/**")
        .build();
  }
  @Bean
  public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion, @Value("${swagger.api-server-url}") String apiServerUrl, @Value("${swagger.description}") String description) {

    SecurityScheme securityScheme = new SecurityScheme().type(Type.HTTP)
        .scheme("bearer").bearerFormat("JWT").in(In.HEADER).name("Authorization");

    Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);

    List<SecurityRequirement> securityRequirements = Arrays.asList(new SecurityRequirement().addList("bearerAuth"));


    Info info = new Info().title("Modong API Docs")
        .description("모두의 동아리 서비스에서 사용하는 API를 정리한 문서").version(appVersion);

    List<Server> servers = Arrays.asList(makeServer(apiServerUrl, description));

    return new OpenAPI()
        .components(components)
        .security(securityRequirements)
        .info(info)
        .servers(servers);
  }
  @Bean
  public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
      WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
      EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
    List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
    Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
    allEndpoints.addAll(webEndpoints);
    allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
    allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
    String basePath = webEndpointProperties.getBasePath();
    EndpointMapping endpointMapping = new EndpointMapping(basePath);
    boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
    return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(),
        new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
  }


  private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
    return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(
        ManagementPortType.DIFFERENT));
  }

  private Server makeServer(final String url, final String description){
    Server server = new Server();
    server.setUrl(url);
    server.setDescription(description);
    return server;
  }
}
