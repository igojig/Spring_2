package com.geekbrains.spring.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

//@Aspect
//@Component
@Slf4j
public class ServiceAspect {

    @Pointcut("execution (* com.geekbrains.spring.web.services.*.*(..))")
    public void loggingPointcut(){
    }

    @Around("loggingPointcut()")
    public Object loggingAspect(ProceedingJoinPoint pjp) throws Throwable{
        Class<?> beanClass =pjp.getTarget().getClass();
        String methodName=pjp.getSignature().getName();
        Object[] args=pjp.getArgs();

       String argValues= Arrays.stream(args)
                .map(o-> o==null?"null":o.toString())
                .collect(Collectors.joining(", ", "[", "]"));

        Object result=null;
        try {
            result=pjp.proceed();
            log.debug("Method {}#{} with arguments {}", beanClass, methodName, argValues);
        }
        // в ProductController->findAll добавлен вызов TestException
        catch (RuntimeException e){
            log.error("Exception in {}#{} with arguments {}", beanClass, methodName, argValues);
        }
        return result;
    }
}
