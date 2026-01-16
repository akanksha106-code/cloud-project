package org.example.dbs.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        
        logger.info("REQUEST - Method: {}, URI: {}, RemoteAddr: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            request.getRemoteAddr());
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        
        logger.info("RESPONSE - Method: {}, URI: {}, Status: {}, ExecutionTime: {}ms", 
            request.getMethod(), 
            request.getRequestURI(), 
            response.getStatus(), 
            executeTime);
        
        if (ex != null) {
            logger.error("EXCEPTION - Method: {}, URI: {}, Error: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                ex.getMessage(), 
                ex);
        }
    }
}