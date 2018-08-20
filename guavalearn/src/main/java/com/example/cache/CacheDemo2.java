package com.example.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

//第二种 callable callback
//refresh机制：
//        - LoadingCache.refresh(K) 在生成新的value的时候，旧的value依然会被使用。
//        - CacheLoader.reload(K, V) 生成新的value过程中允许使用旧的value
//        - CacheBuilder.refreshAfterWrite(long, TimeUnit) 自动刷新cache
public class CacheDemo2 {

    public static void main(String[] args) {
        Cache<String,String> cache= CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build();
        try {
            String result=cache.get("java", () -> "hello java");
            /*
            cache.get("java", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "hello java";
                }
            });
            */
            System.out.println(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
