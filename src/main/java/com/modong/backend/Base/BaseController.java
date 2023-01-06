package com.modong.backend.Base;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "기본 API")
@RestController
public class BaseController {

  @GetMapping("/")
  @Operation(summary = "헬스체크용")
  public String running() {
    return "Server is running!!";
  }
}
