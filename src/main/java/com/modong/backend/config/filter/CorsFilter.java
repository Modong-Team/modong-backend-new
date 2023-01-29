package com.modong.backend.config.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
  private Logger logger = LoggerFactory.getLogger(CorsFilter.class);

  @Value("${allow-cors.list}")
  private List<String> allowCorsUrl;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpServletResponse res = (HttpServletResponse)response;

    String requestMethod = req.getMethod();
    String remoteUser = req.getRemoteUser();
    String userAgent = req.getHeader("user-agent");
    String host = req.getHeader("host");
    String originUri = req.getRequestURI();
    String originUrl = req.getHeader("Origin");

    if(originUrl == null) {
      res.setHeader("Access-Control-Allow-Origin", "*");
    }
    else {
      for(String allowedUrl : allowCorsUrl) {
        // Modify Header if [origin URL] exists in [allowed URL list]
        if(originUrl.startsWith(allowedUrl)) {
          res.setHeader("Access-Control-Allow-Origin", originUrl);
          break;
        }
      }
    }

    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader("Access-Control-Allow-Methods","*");
    res.setHeader("Access-Control-Max-Age", "3600");
    res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");

    if("OPTIONS".equalsIgnoreCase(req.getMethod())) res.setStatus(HttpServletResponse.SC_OK);
    else chain.doFilter(request, response);
  }
  
  public String getClientIP(HttpServletRequest request) {
    String[] httpHeaderList = {"X-Forwarded-For", "Proxy-Client-IP",
            "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
    String ip = null;

    // check each HTTP header
    for(String httpHeader : httpHeaderList) {
      if(ip == null) {
        ip = request.getHeader(httpHeader);
        logger.info("> " + httpHeader + " : " + ip);
      }
    }

    if(ip == null) {
      ip = request.getRemoteAddr();
      logger.info("> getRemoteAddr : " + ip);
    }
    logger.info("> [Result] IP Address : " + ip);

    return ip;
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
