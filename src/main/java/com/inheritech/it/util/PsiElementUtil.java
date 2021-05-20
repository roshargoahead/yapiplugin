package com.inheritech.it.util;

import com.inheritech.it.constants.AnnotationConstants;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.compress.utils.Lists;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-05-12 10:48:16
 */
public class PsiElementUtil {
    private PsiElementUtil() {
    }

    /**
     * 获取方法注解
     *
     * @param psiMethod
     * @return
     */
    public static PsiAnnotation getMethodAnnotation(PsiMethod psiMethod) {
        if (Objects.nonNull(psiMethod)) {
            Set<String> pathSet = AnnotationConstants.getPathSet();
            for (String annotation : pathSet) {
                if (psiMethod.hasAnnotation(annotation)) {
                    return psiMethod.getAnnotation(annotation);
                }
            }
        }
        return null;
    }

    /**
     * 获取类注解
     *
     * @param psiClass
     * @return
     */
    public static PsiAnnotation getClassAnnotation(PsiClass psiClass) {
        if (Objects.nonNull(psiClass)) {
            Set<String> pathSet = AnnotationConstants.getPathSet();
            for (String annotation : pathSet) {
                if (psiClass.hasAnnotation(annotation)) {
                    return psiClass.getAnnotation(annotation);
                }
            }
        }
        return null;
    }

    public static List<Map<String, Object>> getMethodParamAnnotation(PsiMethod psiMethod) {
        if (Objects.isNull(psiMethod)) {
            return Lists.newArrayList();
        }
        List<Map<String, Object>> result = new ArrayList<>();
        PsiParameterList parameterList = psiMethod.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        Set<String> paramSet = AnnotationConstants.getParamSet();
        for (PsiParameter parameter : parameters) {
            for (String annotation : paramSet) {
                if (parameter.hasAnnotation(annotation) && annotation.contains("Param")) {
                    PsiAnnotation psiAnnotation = parameter.getAnnotation(annotation);
                    String name = parameter.getName();
                    PsiType type = parameter.getType();
                    String typeName = type.toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("type", typeName);
                    map.put("annotation", psiAnnotation);
                    result.add(map);
                }
            }
        }
        return result;
    }
}