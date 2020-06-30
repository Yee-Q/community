package com.yeexang.community.advice;

import com.yeexang.community.exception.CustomizeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handle(HttpServletRequest request, Exception ex) {
        ModelAndView mv = new ModelAndView();
        if (ex instanceof CustomizeException) {
            mv.addObject("message", ex.getMessage());
        } else {
            mv.addObject("message", "服务器太热了，要不然你稍后再试试？");
        }
        mv.setViewName("error");
        return mv;
    }
}
