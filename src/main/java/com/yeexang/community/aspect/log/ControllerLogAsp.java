package com.yeexang.community.aspect.log;

import com.yeexang.community.common.http.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Controller 日志切面
 *
 * @author yeeq
 * @date 2021/8/4
 */
@Slf4j
@Aspect
@Component
public class ControllerLogAsp {

    @Pointcut("execution(* com.yeexang.community.web.controller.*.*(..))")
    public void controllerMethod() {

    }

    @Before(value = "controllerMethod()", argNames = "joinPoint")
    public void LogRequestInfo(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String typeName = signature.getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        sb.append(typeName).append("controller log start: ")
                .append(methodName).append("(");
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                if (i == args.length - 1) {
                    sb.append(args[i].toString()).append(")");
                } else {
                    sb.append(args[i].toString()).append(",");
                }
            } else {
                if (i == args.length - 1) {
                    sb.append("null").append(")");
                } else {
                    sb.append("null").append(",");
                }
            }
        }
        log.info(sb.toString());
    }

    @AfterReturning(returning = "responseEntity", pointcut = "controllerMethod()")
    public void logResultInfo(JoinPoint joinPoint, ResponseEntity responseEntity) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String typeName = signature.getDeclaringTypeName();
        String sb = typeName + "controller log end: " +
                typeName + " " +
                methodName + " " +
                "returnValue: " + responseEntity.toString();
        log.info(sb);
    }
}