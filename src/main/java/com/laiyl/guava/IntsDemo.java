package com.laiyl.guava;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;

import java.util.List;

public class IntsDemo {
    public static void main(String[] args) {
        List<Integer> integers = Ints.asList(1, 2, 3, 4);
        System.out.println(integers);

        int[] concat = Ints.concat(new int[]{1, 2}, new int[]{2, 3, 4});
        System.out.println(concat.length);

        System.out.println(Ints.max(concat));

        System.out.println(Ints.contains(concat,5));


        Multimap<String,String> map = ArrayListMultimap.create();
        map.put("zhangsan", "man");
        map.put("zhangsan", "yes");
        map.put("lucy", "woman");
        System.out.println(map.get("zhangsan"));


    }
}
