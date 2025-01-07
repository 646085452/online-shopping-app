package com.cjq.onlineshoppingapp.log;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // service layer
    @Pointcut("execution(* com.cjq.onlineshoppingapp.service.*.*(..))")
    public void serviceLayerMethods() {}

    @Before("serviceLayerMethods()")
    public void logBeforeServiceMethod() {
        logger.info("Executing method in service layer...");
    }
}

