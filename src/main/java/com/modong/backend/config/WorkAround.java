package com.modong.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

public class WorkAround {

//  @Override
//  public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
//    OpenAPI openApi = context.getSpecification();
//    Server localServer = new Server();
//    localServer.setDescription("local");
//    localServer.setUrl("http://localhost:8080");
//
//    Server prodServer = new Server();
//    prodServer.setDescription("prod");
//    prodServer.setUrl("https://api.exhelper.site");
//    openApi.setServers(Arrays.asList(prodServer,localServer));
//    return openApi;
//  }
//
//  @Override
//  public boolean supports(DocumentationType documentationType) {
//    return documentationType.equals(DocumentationType.OAS_30);
//  }
}