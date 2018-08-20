package com.example.collection;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.junit.Test;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FilterDemo {

    //Collections2
    //filter（）：只保留集合中满足特定要求的元素
    //这里找单词反转后和原来一致的元素
    @Test
    public void test1(){
        List<String> list= Lists.newArrayList("moon","dad","refer","son");
        Collection<String> palindromeList= Collections2.filter(list, input -> {
            return new StringBuilder(input).reverse().toString().equals(input); //找回文串
        });

        /* 上面为简化的匿名内部类
        Collection<String> list2 = Collections2.filter(list, new Predicate<String>() {
            @Override
            public boolean apply(@NullableDecl String s) {

                return new StringBuilder(s).reverse().toString().equals(s);
            }
        });*/

        System.out.println(palindromeList);
    }

    //transform（）：类型转换
    @Test
    public void test2(){
        Set<Long> times= Sets.newHashSet();
        times.add(2123124342L);
        times.add(9320001010L);
        times.add(9920170621L);
//        Collection<String> timeStrCol= Collections2.transform(times, new Function<Long, String>() {
//            @Nullable
//            @Override
//            public String apply(@Nullable Long input) {
//                return new SimpleDateFormat("yyyy-MM-dd").format(input);
//            }
//        });
        Collection<String> timeStrCol = Collections2.transform(times,
                param -> new SimpleDateFormat("yyyy-MM-dd").format(param));
        System.out.println(timeStrCol);
    }

}
