package com.example.springframe.utils;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class DomainConvertUtil {

    private DomainConvertUtil() {}

    public static <T> T getCopyObject(Class<T> targetClass,Object ... sourceObject) {
        try {
            T targetObject = targetClass.newInstance();
            Arrays.stream(sourceObject).filter(DomainConvertUtil::isNotNull).forEach(s -> BeanUtils.copyProperties(s, targetObject));
            return targetObject;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static <T> Page<T> getPage(Class<T> targetClass,Page<?> sourceObject){
        List<T> targetList = new ArrayList<>();
        sourceObject.forEach(s -> targetList.add(getCopyObject(targetClass, s)));
        return new PageImpl<>(targetList, sourceObject.getPageable(), sourceObject.getTotalElements());
    }
    public static <T> Page<T> getPageHt(Class<T> targetClass,Page<?> sourceObject){
        List<T> targetList = new ArrayList<>();
        sourceObject.forEach(s -> targetList.add(BeanUtil.toBean(s,targetClass)));
        return new PageImpl<>(targetList, sourceObject.getPageable(), sourceObject.getTotalElements());
    }

    public static <T> List<T> getList(Class<T> targetClass,Page<?> sourceObject){
        List<T> targetList = new ArrayList<>();
        sourceObject.forEach(s -> targetList.add(getCopyObject(targetClass, s)));
        return targetList;
    }

    public static <T> List<T> getList(Class<T> targetClass, Collection<?> sourceObject){
        List<T> targetList = new ArrayList<>();
        sourceObject.forEach(s -> targetList.add(getCopyObject(targetClass, s)));
        return targetList;
    }

    public static <T> T getCopyObject(T targetObject,Object ... sourceObject) {
        Arrays.stream(sourceObject).filter(DomainConvertUtil::isNotNull).forEach(s -> BeanUtils.copyProperties(s, targetObject));
        return targetObject;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }
}
