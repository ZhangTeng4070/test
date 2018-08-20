package com.example.string;

import com.google.common.base.CharMatcher;
import org.junit.Test;

import java.io.InputStream;

public class CharMatcherTest {

    @Test
    public void digit(){
        boolean b = CharMatcher.digit().matchesAllOf("a123");
        System.out.println(b);
    }



}
