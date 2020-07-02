package com.yeexang.community.advice;

import com.yeexang.community.dto.ResponseDTO;
import com.yeexang.community.exception.CustomizeException;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handle(HttpServletRequest request, Exception ex) {
        ModelAndView mv = new ModelAndView();
        String contentType = request.getContentType();
        ResponseDTO error = new ResponseDTO();
        if (ex instanceof CustomizeException) {
            error.setCustomizeExMsg(ex.getMessage());
            mv.addObject("error", error);
        } else {
            error.setCustomizeExMsg(ErrorConstant.DEFAULT_ERROR_MSG);
            mv.addObject("error", error);
        }
        mv.setViewName("error");
        return mv;
    }
}
