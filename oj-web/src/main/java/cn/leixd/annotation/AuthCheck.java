package cn.leixd.interceptor.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheck {
    RequiredType[] value() default RequiredType.LOGIN;
}
