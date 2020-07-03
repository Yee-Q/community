package com.yeexang.community.advice;

import com.yeexang.community.dto.ResultDTO;
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
        if (ex instanceof CustomizeException) {
            ResultDTO resultDTO = ResultDTO.getErrorResult(ex.getMessage());
            mv.addObject("error", resultDTO);
        } else {
            ResultDTO resultDTO = ResultDTO.getErrorResult(ErrorConstant.DEFAULT_ERROR_MSG);
            mv.addObject("error", resultDTO);
        }
        mv.setViewName("error");
        return mv;
    }
}
