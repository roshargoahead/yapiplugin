package com.inheritech.it.constants;

import java.util.HashSet;
import java.util.Set;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-05-12 09:40:44
 */
public class AnnotationConstants {
    public static final Set<String> PATH_SET = new HashSet<>();

    public static final Set<String> PARAM_SET = new HashSet<>();


    public static final String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";

    public static final String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";

    public static final String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";

    public static final String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";

    public static final String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";

    public static final String PATCH_MAPPING = "org.springframework.web.bind.annotation.PatchMapping";

    public static final String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";

    public static final String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";

    public static final String REQUEST_HEADER = "org.springframework.web.bind.annotation.RequestHeader";

    public static final String REQUEST_ATTRIBUTE = "org.springframework.web.bind.annotation.RequestAttribute";

    public static final String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";

    public static final String NOT_NULL = "javax.validation.constraints.NotNull";

    public static final String NOT_EMPTY = "javax.validation.constraints.NotEmpty";

    public static final String NOT_BLANK = "javax.validation.constraints.NotBlank";

    static {
        // 路径相关注解
        PATH_SET.add(REQUEST_MAPPING);
        PATH_SET.add(GET_MAPPING);
        PATH_SET.add(POST_MAPPING);
        PATH_SET.add(PUT_MAPPING);
        PATH_SET.add(DELETE_MAPPING);
        PATH_SET.add(PATCH_MAPPING);
        // 参数相关注解
        PARAM_SET.add(REQUEST_BODY);
        PARAM_SET.add(REQUEST_PARAM);
        PARAM_SET.add(PATH_VARIABLE);
    }

    public static Set<String> getPathSet() {
        return new HashSet<>(PATH_SET);
    }

    public static Set<String> getParamSet() {
        return new HashSet<>(PARAM_SET);
    }




}