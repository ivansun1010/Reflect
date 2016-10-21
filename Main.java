package com.bj58;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 58 on 2016/8/6.
 */
public class Main {
    public static void main(String[] args) {
        Pattern p = Pattern.compile(".*\\[(.*)\\]");

        ClassFilter filter;
        filter = new ClassFilter(){
            public boolean accept(Class clazz) {
                return true;
            }
        };
        for (Class clazz : ClassUtils.scanPackage("com.bj58", filter)) {
            boolean classPath=true;
            Annotation clazzAnnotations[] = clazz.getAnnotations();
            String classpath = "";
            for(Annotation clazzAnnotation : clazzAnnotations) {
                if(clazzAnnotation.annotationType().getName().equals("com.bj58.wf.mvc.annotation.Path")) {
                    String classpathTemp = clazzAnnotation.toString();

                    Matcher m  = p.matcher(classpathTemp);
                    if(m.find()) {
                        classpath = m.group(1);
                    }
                }
            }
            if(classPath ==true) {
                Method methods[] = clazz.getMethods();
                for (Method method : methods) {
                    boolean hasPath = false;
                    boolean hasAnn = false;
                    boolean hasAuthenticate = false;
                    String methodpath = "";
                    if (method.getModifiers() == 1) {
                        Annotation annotations[] = method.getAnnotations();
                        for (Annotation annotation : annotations) {
                            String annotaion = annotation.annotationType().getName();
                            if(annotaion.equals("com.bj58.wf.mvc.annotation.Path")) {
                                hasAuthenticate = true;
                            }
                            if(annotaion.equals("com.bj58.zhaopin.zcm.api.interceptors.Authenticate")) {
                                hasAnn = true;
                            }

                            if(hasAuthenticate) {
                                if(!hasAnn) {
                                    String methodpathTemp = annotation.toString();

                                    Matcher m  = p.matcher(methodpathTemp);
                                    if(m.find()) {
                                        methodpath = m.group(1);
//                                        System.out.println("class name :\t"+clazz.getName()+"\t,method name :\t"+method.getName()+"\t,path value :\t"+classpath+methodpath);
                                    }
                                }

                            }
                        }
                        if(hasAuthenticate){
                            if(!hasAnn) {
                                System.out.println("class name :\t"+clazz.getName()+"\t,method name :\t"+method.getName()+"\t,path value :\t"+classpath+methodpath);
                            }
                        }
                    }
                }
            }

        }
    }
}
