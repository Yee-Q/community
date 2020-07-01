package com.yeexang.community.controller;

import com.yeexang.community.dto.ErrorMsgDTO;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorCon implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, Model model) {
        ErrorMsgDTO error = new ErrorMsgDTO();
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {
            error.setCustomizeExMsg(ErrorConstant.REQUEST_ERROR);
            model.addAttribute("error", error);
        }
        if (status.is5xxServerError()) {
            error.setCustomizeExMsg(ErrorConstant.DEFAULT_ERROR_MSG);
            model.addAttribute("error", error);
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
