package com.oceanli.gupao.spring.demo.controller;

import com.oceanli.gupao.spring.demo.service.IModifyService;
import com.oceanli.gupao.spring.demo.service.IQueryService;
import com.oceanli.gupao.spring.demo.service.impl.CglibService;
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

    @GPAutowired
    IModifyService modifyService;

    @GPAutowired("cglibService")
    CglibService cglibService;

    @GPRequestMapping("/query.json")
    public GPModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @GPRequestParam("name") String name){
        String result = queryService.query(name);
        return out(response,result);
    }

    @GPRequestMapping("/add*.json")
    public GPModelAndView add(HttpServletRequest request,HttpServletResponse response,
                              @GPRequestParam("name") String name,@GPRequestParam("addr") String addr){
        String result = modifyService.add(name,addr);
        return out(response,result);
    }
    @GPRequestMapping("/remove.json")
    public GPModelAndView remove(HttpServletRequest request,HttpServletResponse response,
                                 @GPRequestParam("id") Integer id){
        String result = modifyService.remove(id);
        return out(response,result);
    }
    @GPRequestMapping("/edit.json")
    public GPModelAndView edit(HttpServletRequest request,HttpServletResponse response,
                               @GPRequestParam("id") Integer id,
                               @GPRequestParam("name") String name){
        String result = modifyService.edit(id,name);
        return out(response,result);
    }

    @GPRequestMapping("/cglib.json")
    public GPModelAndView cglibTest(HttpServletRequest request,HttpServletResponse response,
                               @GPRequestParam("name") String name){
        String result = cglibService.get(name);
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
