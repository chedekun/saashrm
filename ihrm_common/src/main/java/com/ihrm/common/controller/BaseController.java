package com.ihrm.common.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 846602483
 * @Date: 2019-8-4 12:26
 * @Version 1.0
 * 公共controller
 */

/**
 * 获取request，response
 * 获取企业id，获取企业名称
 */
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @ModelAttribute
    public void setReqAndResp(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /**
     * 企业id，暂时用1，以后会动态获取
     * @return
     */
    public String parseCompanyId(){
        return "1";
    }
    public String parseCompanyName(){
        return "西安外事学院";
    }
}
