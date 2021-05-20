package com.inheritech.it.constants;

import java.util.HashSet;
import java.util.Set;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-05-19 14:07:42
 */
public class JavaBasicType {

    public static final Set<String> TYPE_SET = new HashSet<String>() {
        {
            add("java.lang.String");
            add("byte");
            add("short");
            add("int");
            add("long");
            add("double");
            add("float");
            add("char");
            add("boolean");
            add("java.lang.Integer");
            add("java.lang.Long");
            add("java.lang.Double");
            add("java.lang.Float");
            add("java.lang.Boolean");
            add("java.lang.Character");
        }
    };

}