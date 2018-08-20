package com.example.guavalearn;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class OptionalTest1 {

    public static void main(String[] args) {
        Integer value1=null;
        Integer value2=10;
       /*创建指定引用的Optional实例，若引用为null则快速失败返回absent()
         absent()创建引用缺失的Optional实例
        */
        Optional<Integer> a=Optional.fromNullable(value1); //fromNullable可以为空，
                                                           // 如果是of就会报错
        Optional<Integer> b=Optional.of(value2); //返回包含给定的非空引用Optional实例
        System.out.println(sum(a,b));

    }

    private static Integer sum(Optional<Integer> a,Optional<Integer> b){
        //isPresent():如果Optional包含非null的引用（引用存在），返回true
        System.out.println("First param is present: "+a.isPresent());
        System.out.println("Second param is present: "+b.isPresent());
        Integer value1=a.or(1);  //a是空的话的默认值
        Integer value2=b.get(); //返回所包含的实例,它必须存在,通常在调用该方法时会调用isPresent()判断是否为null
        return value1+value2;
    }

}
