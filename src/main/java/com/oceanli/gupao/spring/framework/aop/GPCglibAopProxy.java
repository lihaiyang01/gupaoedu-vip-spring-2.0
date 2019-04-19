package com.oceanli.gupao.spring.framework.aop;


public class GPCglibAopProxy implements GPAopProxy {

    private GPAdvisedSupport config;

    public GPCglibAopProxy(GPAdvisedSupport config) {
        this.config = config;
    }

    public GPAdvisedSupport getConfig() {
        return config;
    }

    public void setConfig(GPAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }

}
