package com.example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericDAO<T> {

    String nameSpace;

    private Class<T> entityClass;
    protected GenericDAO() {
        Type type = getClass().getGenericSuperclass();
        System.out.println(type);
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        System.out.println(trueType);
        this.entityClass = (Class<T>) trueType;

        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            this.nameSpace = ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
            System.out.println(nameSpace);
        } else {
            this.nameSpace = ((Class)((ParameterizedType)this.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
            System.out.println(nameSpace);
        }

    }

}
