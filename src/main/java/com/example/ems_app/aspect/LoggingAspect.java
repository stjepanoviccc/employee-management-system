package com.example.ems_app.aspect;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Before("execution (* com.example.ems_app.service.EmployeeService.*(..))")
    public void logMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info(String.format("Method [%s] executed with arguments %s", methodName, Arrays.toString(args)));
    }
}