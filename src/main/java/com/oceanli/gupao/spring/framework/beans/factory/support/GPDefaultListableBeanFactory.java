package com.oceanli.gupao.spring.framework.beans.factory.support;

import com.oceanli.gupao.spring.framework.annotation.GPAutowired;
import com.oceanli.gupao.spring.framework.annotation.GPController;
import com.oceanli.gupao.spring.framework.annotation.GPService;
import com.oceanli.gupao.spring.framework.beans.factory.GPBeanFactory;
import com.oceanli.gupao.spring.framework.beans.factory.config.GPBeanDefinition;
import com.oceanli.gupao.spring.framework.beans.factory.config.GPBeanPostProcessor;
import com.oceanli.gupao.spring.framework.beans.factory.config.GPBeanWrapper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GPDefaultListableBeanFactory implements GPBeanFactory {

    public Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);

    @Override
    public Object getBean(String beanName) {

        GPBeanDefinition gpBeanDefinition = this.beanDefinitionMap.get(beanName);
        GPBeanWrapper instanceWrapper = null;
        if (gpBeanDefinition.isSingleton()) {
            instanceWrapper = this.factoryBeanInstanceCache.get(beanName);
        }
        if (instanceWrapper == null) {
            instanceWrapper = initializeBean(gpBeanDefinition);
            factoryBeanInstanceCache.put(beanName, instanceWrapper);

        }
        Object bean = instanceWrapper.getWrappedInstance();

        try {
            //将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象
            populateBean(beanName, bean);

            GPBeanPostProcessor gpBeanPostProcessor = new GPBeanPostProcessor();
            //在实例初始化以前调用一次
            gpBeanPostProcessor.postProcessBeforeInitialization(bean, beanName);

            //调用Bean实例对象初始化的方法，这个初始化方法是在Spring Bean定义配置
            //文件中通过init-method属性指定的
            invokeInitMethods(beanName, instanceWrapper);
            //在实例初始化以后调用一次
            gpBeanPostProcessor.postProcessAfterInitialization(bean, beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    private void invokeInitMethods(String beanName, GPBeanWrapper instanceWrapper) {
    }

    private void populateBean(String beanName, Object bean) {
        Class<?> clazz = bean.getClass();
        //不是所有的牛奶都是特仑苏
        if (!clazz.isAnnotationPresent(GPController.class)
                && !clazz.isAnnotationPresent(GPService.class)) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)) {
                continue;
            }
            GPAutowired annotation = field.getAnnotation(GPAutowired.class);
            String autowiredName = annotation.value();
            if (autowiredName == null || "".equals(autowiredName)) {
                autowiredName = field.getType().getName();
            }
            GPBeanWrapper beanWrapper = this.factoryBeanInstanceCache.get(autowiredName);
            if (beanWrapper == null) {
                Object autowiredBean = getBean(autowiredName);
                beanWrapper = new GPBeanWrapper(autowiredBean);
            }
            field.setAccessible(true);
            try {
                field.set(bean, beanWrapper.getWrappedInstance());
            } catch (IllegalAccessException e) {

            }
        }
    }

    private GPBeanWrapper initializeBean(GPBeanDefinition gpBeanDefinition) {

        String beanClassName = gpBeanDefinition.getBeanClassName();

        try {
            Class<?> clazz = Class.forName(beanClassName);
            GPBeanWrapper beanWrapper = new GPBeanWrapper(clazz.newInstance());
            return beanWrapper;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }
}
