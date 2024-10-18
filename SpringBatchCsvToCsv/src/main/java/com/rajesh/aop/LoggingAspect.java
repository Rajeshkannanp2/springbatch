package com.rajesh.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* org.springframework.batch.core.JobLauncher.run(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Batch Job started: " + joinPoint.getSignature().getName());
    }

    @AfterReturning("execution(* org.springframework.batch.core.JobLauncher.run(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Batch Job completed: " + joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* org.springframework.batch.core.JobLauncher.run(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Exception in batch job: " + joinPoint.getSignature().getName(), error);
    }
}
