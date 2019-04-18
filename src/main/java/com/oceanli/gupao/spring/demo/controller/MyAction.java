package com.oceanli.gupao.spring.demo.controller;

import com.oceanli.gupao.spring.demo.service.IQueryService;
import com.oceanli.gupao.spring.framework.annotation.GPAutowired;
import com.oceanli.gupao.spring.framework.annotation.GPController;
import com.oceanli.gupao.spring.framework.annotation.GPRequestMapping;
import com.oceanli.gupao.spring.framework.annotation.GPRequestParam;
import com.oceanli.gupao.spring.framework.webmvc.servlet.GPModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@GPController
@GPRequestMapping("/web")
public class MyAction {

    @GPAutowired
    IQueryService queryService;

    @GPRequestMapping("/query.json")
    public GPModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @GPRequestParam("name") String name){
        String result = queryService.query(name);
        return out(response,result);
    }

    private GPModelAndView out(HttpServletResponse resp, String str){
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
