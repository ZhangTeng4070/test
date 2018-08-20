package com.example.collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;

public class MultiMapDemo {

    public static void main(String[] args) {
        Multimap<String,Integer> map= HashMultimap.create();
                              //Multimap是把键映射到任意多个值的一般方式
        map.put("a",1); //key相同时不会覆盖原value
        map.put("a",2);
        map.put("a",3);
        System.out.println(map); //{a=[1, 2, 3]}
        System.out.println(map.get("a")); //返回的是集合
        System.out.println(map.size()); //返回所有”键-单个值映射”的个数,而非不同键的个数
        System.out.println(map.keySet().size()); //返回不同key的个数
        Map<String, Collection<Integer>> mapView=map.asMap();
        System.out.println(mapView);

    }

}
