package com.oceanli.gupao.spring.demo.service.impl;

import com.oceanli.gupao.spring.demo.service.IModifyService;
import com.oceanli.gupao.spring.framework.annotation.GPService;

@GPService
public class ModifyService implements IModifyService {
    @Override
    public String add(String name, String addr) {
        return "modifyService add,name=" + name + ",addr=" + addr;
    }

    @Override
    public String edit(Integer id, String name) {
        return "modifyService edit,id=" + id + ",name=" + name;
    }

    @Override
    public String remove(Integer id) {
        return "modifyService id=" + id;
    }
}
