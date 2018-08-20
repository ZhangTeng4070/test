package com.example.other;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

//多个Function组合
public class TransformDemo {

    public static void main(String[] args) {
        List<String> list= Lists.newArrayList("abcde","good","happiness");
        //确保容器中的字符串长度不超过5
        /*Function<String,String> f1=new Function<String, String>() {
            @Nullable
            @Override
            public String apply(@Nullable String input) {
                return input.length()>5?input.substring(0,5):input;
            }
        };*/
        //转成大写
        /*Function<String,String> f2=new Function<String, String>() {
            @Nullable
            @Override
            public String apply(@Nullable String input) {
                System.out.println(input.toUpperCase());
                return input.toUpperCase();
            }
        };*/
        Function<String,String> f1 = x->x.length()>5?x.substring(0,5):x;
        Function<String,String> f2 = x-> x.toUpperCase();

        Function<String,String> function= Functions.compose(f1,f2);

        Collection<String> results= Collections2.transform(list,function);
        System.out.println(results);
    }

}
